import {Box, Card, CardContent, IconButton, List} from "@mui/joy";
import AddBoxIcon from "@mui/icons-material/AddBox";
import {ChatResponseFragment} from "@/generated/graphql";
import ChatListItem from "@/components/chat/ChatListItem";
import {useState} from "react";
import CreateChatModal from "@/components/chat/CreateChatModal";

interface ChatListProps {
    chats: ChatResponseFragment[];
    selected: number;
    setSelected: (id: number) => void;
}

const ChatList = ({chats, selected, setSelected}: ChatListProps) => {

    const [modalOpen, setModalOpen] = useState<boolean>(false);


    return (
        <>
            <Card>
                <CardContent>
                    <Box sx={{ px: 2, pb: 1.5 }}>
                        <IconButton onClick={() => setModalOpen(true)}>
                            <AddBoxIcon />
                        </IconButton>
                    </Box>
                    <List
                        sx={{
                            py: 0,
                            '--ListItem-paddingY': '0.75rem',
                            '--ListItem-paddingX': '1rem',
                        }}
                    >
                        {chats.map((c) => (
                            <ChatListItem
                                key={c.chat.id}
                                chat={c}
                                selected={c.chat.id === selected}
                                setSelected={() => setSelected(c.chat.id)}
                            />
                        ))}
                    </List>
                </CardContent>
            </Card>
            {modalOpen && (
                <CreateChatModal onClose={() => setModalOpen(false)} />
            )}
        </>
    );
}

export default ChatList;