import {createContext, useContext} from "react";
import {ChatMessage} from "@/generated/graphql";


export const UnreadMessagesContext = createContext<ChatMessage[]>([]);

const useUnreadMessages = () => useContext(UnreadMessagesContext);

export default useUnreadMessages;