'use client';
import {Box} from "@mui/joy";
import Sidebar from "@/components/Sidebar";
import PathDisplay from "@/components/PathDisplay";
import {ReactNode, useCallback, useEffect, useMemo, useState} from "react";
import NoSSR from "@/components/NoSSR";
import  { SettingsContext } from "@/hooks/useSettings";
import {
    ChatResponseFragment,
    SettingDataFragment, useGetAllChatsLazyQuery,
    useGetAllChatsQuery,
    useGetAllSettingsQuery,
    useGetCurrentUserQuery
} from "@/generated/graphql";
import {usePathname, useRouter} from "next/navigation";
import {CurrentUserContext} from "@/hooks/useCurrentUser";
import {useCookies} from "react-cookie";
import {UnreadMessagesContext} from "@/hooks/useUnreadMessages";

interface BaseLayoutProps {
    children: ReactNode;
}

/**
 * Base layout
 *
 * @constructor
 */
const BaseLayout = ({children}: BaseLayoutProps) => {
    const {data} = useGetAllSettingsQuery();
    const {data: userData} = useGetCurrentUserQuery();
    const pathname = usePathname();
    const {data: allChatsData, refetch: refetchChats} = useGetAllChatsQuery();
    const [unreadMessages, setUnreadMessages] = useState<ChatResponseFragment[]>([]);
    const [cookies] = useCookies(['jwt']);

    const displaySidebar = useMemo(
        () => !(pathname.includes("login") || pathname.includes("reset")),
        [pathname]
    );

    const messageListener = useCallback((msg: MessageEvent<any>) => {
        const json = JSON.parse(msg.data as string);
        console.log(unreadMessages);
        if (json.reason === "NEW_MESSAGE") {
            const others = unreadMessages.filter((s) => s.chat.id !== json.message.chatId);
            let chat = unreadMessages.find((s) => s.chat.id === json.message.chatId);
            console.log(unreadMessages);
            if (chat !== undefined) {
                chat = {...chat, unreadMessagesCount: ++chat.unreadMessagesCount};
                setUnreadMessages([...others, chat!]);
            } else {
                setUnreadMessages([...others]);
            }
        } else if (json.reason === "NEW_CHAT") {
            // Refetches all chats - I know this is not performant
            refetchChats();
        }
    }, [unreadMessages]);



    useEffect(() => {
        if (displaySidebar) {
            const baseUrl = process.env.NODE_ENV === 'production' ? '/api/chat/' : 'http://localhost:8080/api/chat/';
            const ws = new WebSocket(
                baseUrl + cookies.jwt
            );
            ws.onmessage = messageListener;
        }
    }, [messageListener, displaySidebar, cookies])

    useEffect(() => {
        const chats = (allChatsData?.userChats ?? []) as ChatResponseFragment[];
        const unread = chats.map((c) => ({
            ...c
        }));
        console.log(unread);
        setUnreadMessages(unread);
    }, [allChatsData]);

    useEffect(() => {
        if (data?.allSettings) {
            let lang = (data?.allSettings ?? []).filter((s) => s?.key === "language")[0]?.value ?? "en";
            window.localStorage.setItem("next-export-i18n-lang", lang.toLocaleLowerCase());
            const event = new Event("localStorageLangChange");
            document.dispatchEvent(event);
        }
    }, [data?.allSettings]);

    return (
        <NoSSR>
            <CurrentUserContext.Provider value={userData?.currentUser ?? null}>
                <SettingsContext.Provider value={(data?.allSettings ?? []) as SettingDataFragment[]}>
                    <UnreadMessagesContext.Provider value={{value: unreadMessages, setValue: setUnreadMessages}}>
                        <Box sx={{ display: 'flex', minHeight: '100dvh' }}>
                            {displaySidebar && (
                                <Sidebar />
                            )}
                            <Box
                                component="main"
                                className="MainContent"
                                sx={{
                                    px: { xs: 2, md: 6 },
                                    pt: {
                                        xs: 'calc(12px + var(--Header-height))',
                                        sm: 'calc(12px + var(--Header-height))',
                                        md: 3,
                                    },
                                    pb: { xs: 2, sm: 2, md: 3 },
                                    flex: 1,
                                    display: 'flex',
                                    flexDirection: 'column',
                                    minWidth: 0,
                                    height: '100dvh',
                                    gap: 1,
                                }}
                            >
                                {displaySidebar && (
                                    <PathDisplay />
                                )}
                                {children}
                            </Box>
                        </Box>
                    </UnreadMessagesContext.Provider>
                </SettingsContext.Provider>
            </CurrentUserContext.Provider>
        </NoSSR>
    );
}

export default BaseLayout;