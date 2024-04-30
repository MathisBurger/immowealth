import {createContext, useContext} from "react";
import {SimpleUserFragment} from "@/generated/graphql";


export const CurrentUserContext = createContext<SimpleUserFragment|null>(null);

const useCurrentUser = () => useContext(CurrentUserContext);

export default useCurrentUser;