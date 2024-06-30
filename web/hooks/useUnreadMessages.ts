import {createContext, useContext} from "react";
import {ChatResponseFragment} from "@/generated/graphql";

interface UnreadMessages {
    value: ChatResponseFragment[];
    setValue: (m: ChatResponseFragment[]) => void;
}

export const UnreadMessagesContext = createContext<UnreadMessages>({
    value: [],
    setValue: () => {}
});


const useUnreadMessages = () => useContext(UnreadMessagesContext);

export default useUnreadMessages;