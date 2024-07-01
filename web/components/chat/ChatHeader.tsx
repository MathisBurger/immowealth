import {Avatar, Stack, Typography} from "@mui/joy";
import useCurrentUser from "@/hooks/useCurrentUser";
import {useMemo} from "react";
import {ChatFragment, ChatResponseFragment} from "@/generated/graphql";
import {useTranslation} from "next-export-i18n";

interface ChatHeaderProps {
    chat: ChatResponseFragment;
}

const ChatHeader = ({chat}: ChatHeaderProps) => {

    const currentUser = useCurrentUser();
    const {t} = useTranslation();

    const chatName = useMemo<string>(() => {
        if (chat.chat.renterChat) {
            return `${t('common.object')} ${chat.chat.realEstateObject?.id}`
        }
        let withoutCurrent = chat.chat.participants.filter((p) => p?.id !== currentUser?.id);
        let usernames = withoutCurrent.map((u) => u?.username);
        return usernames.join(", ");
    }, [currentUser, chat]);

    return (
        <Stack
            direction="row"
            justifyContent="space-between"
            sx={{
                borderBottom: '1px solid',
                borderColor: 'divider',
                backgroundColor: 'background.body',
            }}
            py={{ xs: 2, md: 2 }}
            px={{ xs: 1, md: 2 }}
        >
            <Stack direction="row" spacing={{ xs: 1, md: 2 }} alignItems="center">
                <Avatar
                    variant="outlined"
                    size="sm"
                    src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=286"
                />
                <div>
                    <Typography
                        fontWeight="lg"
                        fontSize="lg"
                        component="h2"
                        noWrap
                        /*endDecorator={
                            sender.online ? (
                                <Chip
                                    variant="outlined"
                                    size="sm"
                                    color="neutral"
                                    sx={{
                                        borderRadius: 'sm',
                                    }}
                                    startDecorator={
                                        <CircleIcon sx={{ fontSize: 8 }} color="success" />
                                    }
                                    slotProps={{ root: { component: 'span' } }}
                                >
                                    Online
                                </Chip>
                            ) : undefined
                        }*/
                    >
                        {chatName}
                    </Typography>
                </div>
            </Stack>
        </Stack>
    );
}

export default ChatHeader;