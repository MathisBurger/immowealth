import {ReactNode} from "react";
import useSnackbar from "@/hooks/useSnackbar";
import {ApolloClient, ApolloLink, ApolloProvider, HttpLink, InMemoryCache} from "@apollo/client";
import {onError} from "@apollo/client/link/error";

interface ApolloBuilderProps {
    children: ReactNode;
}

const ApolloBuilder = ({children}: ApolloBuilderProps) => {

    const {error} = useSnackbar();

    const createHttpLink = () => new HttpLink({uri: 'http://localhost:8080/graphql/'});

    const typenameFilter = () => new ApolloLink((operation, forward) => {
        if (operation.variables) {
            operation.variables = JSON.parse(
                JSON.stringify(operation.variables),
                (key, value) => (key === '__typename' ? undefined : value)
            );
        }
        return forward(operation);
    });

    const globalErrorHandler = () => onError((err) => {
        (err.graphQLErrors ?? []).map((e) => e.message).forEach((e) => error(e));
    });

    const client = new ApolloClient({
        cache: new InMemoryCache(),
        link: ApolloLink.from([
            typenameFilter(),
            globalErrorHandler(),
            createHttpLink(),
        ])
    });


    return (
        <ApolloProvider client={client}>
            {children}
        </ApolloProvider>
    );
}

export default ApolloBuilder;