import {Box, Card, CardContent, IconButton, List} from "@mui/joy";
import AddBoxIcon from "@mui/icons-material/AddBox";
import {ChatFragment} from "@/generated/graphql";
import ChatListItem from "@/components/chat/ChatListItem";

interface ChatListProps {
    chats: ChatFragment[];
    selected: number;
    setSelected: (id: number) => void;
}

const ChatList = ({chats, selected, setSelected}: ChatListProps) => {


    return (
        <Card>
            <CardContent>
                <Box sx={{ px: 2, pb: 1.5 }}>
                    <IconButton>
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
                            key={c.id}
                            chat={c}
                            selected={c.id === selected}
                            setSelected={() => setSelected(c.id)}
                        />
                    ))}
                </List>
            </CardContent>
        </Card>
    );
}

export default ChatList;