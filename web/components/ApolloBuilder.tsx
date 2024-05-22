import {ReactNode, useMemo} from "react";
import useSnackbar from "@/hooks/useSnackbar";
import {ApolloClient, ApolloLink, ApolloProvider, HttpLink, InMemoryCache, ServerError} from "@apollo/client";
import {onError} from "@apollo/client/link/error";
import {useCookies} from "react-cookie";
import {usePathname, useRouter} from "next/navigation";

interface ApolloBuilderProps {
    children: ReactNode;
}

/**
 * Apollo provider
 *
 * @constructor
 */
const ApolloBuilder = ({children}: ApolloBuilderProps) => {

    const {error} = useSnackbar();
    const [cookies] = useCookies(['jwt']);
    const router = useRouter();
    const pathname = usePathname();

    const redirectToLogin = useMemo(
        () => !(pathname.includes("login") || pathname.includes("reset")),
        [pathname]
    );

    const tenantUsername = (data: any) => {
        if (data === null) {
            return null;
        }
        if (typeof data !== "object") {
            return data;
        }
        if (data.__typename === 'User' && data["tenant"] !== null) {
            data["username"] = `${data["username"]} (${data["tenant"]["name"]})`
            return data;
        }
        Object.keys(data).map(key => {
            if (!Array.isArray(data[key]) && typeof data[key] !== "object") return;
            if (Array.isArray(data[key])) {
                data[key] = data[key].map((item: any) => {
                    return tenantUsername(item);
                })
            } else {
                data[key] = tenantUsername(data[key]);
            }
        })
        return data;
    }

    const createHttpLink = () => new HttpLink({
        uri: process.env.NODE_ENV === 'production' ? '/graphql/' : 'http://localhost:8080/graphql/',
        headers: {
            authorization: `Bearer ${cookies.jwt ?? "132"}`
        }
    });

    const typenameFilter = () => new ApolloLink((operation, forward) => {
        if (operation.variables) {
            operation.variables = JSON.parse(
                JSON.stringify(operation.variables),
                (key, value) => (key === '__typename' ? undefined : value)
            );
        }
        return forward(operation).map(response => {
            response.data = tenantUsername(response.data as any);
            return response;
        })
    });

    const globalErrorHandler = () => onError((err) => {
        if ((err?.networkError as ServerError).statusCode === 401) {
            if (redirectToLogin) {
                router.push("/login");
            }
        }
        // @ts-ignore
        if (err.graphQLErrors !== "") {
            (err.graphQLErrors ?? []).map((e) => e.message).forEach((e) => error(e));
        }
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