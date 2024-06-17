"use client";
import {Card, CardContent, Grid, Typography} from "@mui/joy";
import {useTranslation} from "next-export-i18n";
import {ChatFragment, ChatResponseFragment, useGetAllChatsQuery} from "@/generated/graphql";
import ChatList from "@/components/chat/ChatList";
import {useEffect, useMemo, useState} from "react";
import ChatComponent from "@/components/chat/ChatComponent";


const ChatsPage = () => {

    const {t} = useTranslation();
    const [selected, setSelected] = useState<number>(-1);
    const {data} = useGetAllChatsQuery();

    const chatComponent = useMemo(() => {
        const chats = (data?.userChats ?? []) as ChatResponseFragment[];
        const selectedChat = chats.find((c) => c.chat.id === selected);
        if (selectedChat === undefined) {
            return null;
        }
        return <ChatComponent chat={selectedChat} />;
    }, [data, selected]);

    return (
        <>
            <Typography level="h1">{t('common.chats')}</Typography>
            <Grid container direction="row" spacing={2}>
                <Grid xs={3}>
                    <ChatList
                        chats={(data?.userChats ?? []) as ChatResponseFragment[]}
                        selected={selected}
                        setSelected={setSelected}
                    />
                </Grid>
                <Grid xs={9}>
                    {chatComponent}
                </Grid>
            </Grid>
        </>
    );
}

export default ChatsPage;