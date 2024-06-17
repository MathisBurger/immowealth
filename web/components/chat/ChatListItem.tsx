import {Avatar, Box, ListDivider, ListItem, ListItemButton, Stack, Typography} from "@mui/joy";
import {ChatFragment, ChatResponse, ChatResponseFragment} from "@/generated/graphql";
import useCurrentUser from "@/hooks/useCurrentUser";
import React, {CSSProperties, useMemo} from "react";
import {Circle} from "@mui/icons-material";

interface ChatListItemProps {
    chat: ChatResponseFragment;
    selected: boolean;
    setSelected: () => void;
}

const unreadCircleStyle: CSSProperties = {
    color: '#fff',
    backgroundColor: '#4164c1',
    width: '20px',
    height: '20px',
    fontSize: '13px',
    textAlign: 'center',
    borderRadius: '50%'
};

const ChatListItem = ({chat, selected, setSelected}: ChatListItemProps) => {

    const currentUser = useCurrentUser();

    const chatName = useMemo<string>(() => {
        let withoutCurrent = chat.chat.participants.filter((p) => p?.id !== currentUser?.id);
        let usernames = withoutCurrent.map((u) => u?.username);
        return usernames.join(", ");
    }, [currentUser, chat]);

    return (
        <>
            <ListItem>
                <ListItemButton
                    onClick={setSelected}
                    selected={selected}
                    color="neutral"
                    sx={{
                        flexDirection: 'column',
                        alignItems: 'initial',
                        gap: 1,
                    }}
                >
                    <Stack direction="row" spacing={1.5}>
                        <Avatar
                            variant="outlined"
                            size="sm"
                            src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=286"
                        />
                        <Box sx={{ flex: 1 }}>
                            <Typography level="title-sm">{chatName}</Typography>
                        </Box>
                        <Box
                            sx={{
                                lineHeight: 1.5,
                                textAlign: 'right',
                            }}
                        >
                            {chat.unreadMessagesCount > 0 && (
                                <Typography style={unreadCircleStyle}>
                                    {chat.unreadMessagesCount}
                                </Typography>
                            )}
                            <Typography
                                level="body-xs"
                                display={{ xs: 'none', md: 'block' }}
                                noWrap
                            >
                                5 mins ago
                            </Typography>
                        </Box>
                    </Stack>
                </ListItemButton>
            </ListItem>
            <ListDivider sx={{ margin: 0 }} />
        </>
    );
}

export default ChatListItem;