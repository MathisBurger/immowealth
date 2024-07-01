import {createContext, useContext} from "react";
import {ChatResponseFragment} from "@/generated/graphql";

interface Chats {
    value: ChatResponseFragment[];
    setValue: (m: ChatResponseFragment[]) => void;
}

export const ChatsContext = createContext<Chats>({
    value: [],
    setValue: () => {}
});


const useChats = () => useContext(ChatsContext);

export default useChats;