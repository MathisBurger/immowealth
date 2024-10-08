import {
    ChatFragment,
    ChatMessage, ChatResponseFragment, GetAllChatsDocument,
    useGetChatMessagesLazyQuery, useReadMessagesMutation, useSendChatMessageMutation,
} from "@/generated/graphql";
import {Avatar, Box, Button, Card, CardContent, Grid, Input, Sheet, Stack, Textarea} from "@mui/joy";
import ChatHeader from "@/components/chat/ChatHeader";
import {FormEvent, useEffect, useRef, useState} from "react";
import useCurrentUser from "@/hooks/useCurrentUser";
import ChatMessageComponent from "@/components/chat/ChatMessageComponent";
import {useTranslation} from "next-export-i18n";
import useUnreadMessages from "@/hooks/useUnreadMessages";
import useChats from "@/hooks/useChats";

interface ChatComponentProps {
    chat: ChatResponseFragment;
}

const ChatComponent = ({chat}: ChatComponentProps) => {

    const [query] = useGetChatMessagesLazyQuery({
        fetchPolicy: 'network-only'
    });
    const currentUser = useCurrentUser();
    const unreadMessages = useUnreadMessages();
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const {t} = useTranslation();
    const [sendMessageMutation] = useSendChatMessageMutation();
    const [readMessagesMutation] = useReadMessagesMutation({
        refetchQueries: [
            {
                query: GetAllChatsDocument
            }
        ]
    });
    const inputRef = useRef(null);

    const fetchMoreMessages = async () => {
        const res = await query({variables: {chatID: chat.chat.id, limit: 100, maxID: messages[0].id}});
        // @ts-ignore
        setMessages([...(res.data?.chatMessages ?? []), ...messages]);
    }


    const sendMessage = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const data = new FormData(e.currentTarget);
        if (`${data.get("text")}`.trim() !== "") {
            const res = await sendMessageMutation({
                variables: {chatId: chat.chat.id, message: `${data.get("text")}`}
            });
            // @ts-ignore
            inputRef.current.children[0].value = "";
            // @ts-ignore
            setMessages([...messages, res.data?.sendMessage])
        }
    }

    useEffect(() => {
        const unreadLocal = unreadMessages.filter((s) => s.chat?.id === chat.chat.id);
        if (unreadLocal.length > 0 && (messages.length === 0 || unreadLocal[unreadLocal.length-1].id !== messages[messages.length-1].id)) {
             setMessages([...messages, ...unreadLocal]);
        }
    }, [unreadMessages, messages, chat.chat.id]);

    useEffect(() => {
        query({variables: {chatID: chat.chat.id, limit: 100, maxID: null}, fetchPolicy: 'network-only'})
            .then((res) => {
                let msgs = (res.data?.chatMessages ?? []) as ChatMessage[];
                if (msgs.length > 0) {
                    setMessages(msgs);
                } else {
                    setMessages([]);
                }

            })
            .finally(() => {
                if (chat.unreadMessagesCount > 0) {
                    readMessagesMutation({variables: {chatId: chat.chat.id}});
                }
            })
    }, [chat.chat.id]);

    return (
        <Card>
            <CardContent>
                <Sheet
                    sx={{
                        height: '80vh',
                        display: 'flex',
                        flexDirection: 'column',
                        backgroundColor: 'background.level1',
                    }}
                >
                    <ChatHeader chat={chat} />
                    <Box
                        sx={{
                            display: 'flex',
                            flex: 1,
                            minHeight: 0,
                            px: 2,
                            py: 3,
                            overflowY: 'scroll',
                            flexDirection: 'column-reverse',
                        }}
                    >
                        <Stack spacing={2} justifyContent="flex-end">
                            {messages.length >= 100 && (
                                <Button size="sm" color="primary" variant="soft" onClick={fetchMoreMessages}>
                                    {t('common.loadMore')}
                                </Button>
                            )}
                            {messages.map((message: ChatMessage, index: number) => {
                                const isYou = message.sender?.id === currentUser?.id;
                                return (
                                    <Stack
                                        key={index}
                                        direction="row"
                                        spacing={2}
                                        flexDirection={isYou ? 'row-reverse' : 'row'}
                                    >
                                        {!isYou && (
                                            <Avatar
                                                variant="outlined"
                                                size="sm"
                                                src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=286"
                                            />
                                        )}
                                        <ChatMessageComponent message={message} variant={isYou ? 'sent' : 'received'} isYou={isYou} />
                                    </Stack>
                                );
                            })}
                            <form onClick={sendMessage}>
                                <Grid container direction="row" spacing={1}>
                                    <Grid xs={10}>
                                        <Input type="text" name="text" ref={inputRef} />
                                    </Grid>
                                    <Grid xs={2}>
                                        <Button type="submit">{t('common.send')}</Button>
                                    </Grid>
                                </Grid>
                            </form>
                        </Stack>
                    </Box>
                </Sheet>
            </CardContent>
        </Card>
    )
}

export default ChatComponent;