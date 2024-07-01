import {ChatMessage} from "@/generated/graphql";
import {Box, Sheet, Stack, Typography} from "@mui/joy";
import dayjs from "dayjs";

interface ChatMessageComponentProps {
    message: ChatMessage;
    variant: 'sent' | 'received'
    isYou: boolean
}

const ChatMessageComponent = ({message, variant, isYou}: ChatMessageComponentProps) => {

    const isSent = 'sent' === variant;

    return (
        <Box sx={{ maxWidth: '60%', minWidth: 'auto' }}>
            <Stack
                direction="row"
                justifyContent="space-between"
                spacing={2}
                sx={{ mb: 0.25 }}
            >
                <Typography level="body-xs">
                    {isYou ? "" : message?.sender?.username}
                </Typography>
                <Typography level="body-xs">{dayjs(`${message.createdAt}`).format("DD.MM.YYYY HH:mm")}</Typography>
            </Stack>
            <Box
                sx={{ position: 'relative' }}
            >
                <Sheet
                    variant="soft"
                    sx={{
                        p: 1.25,
                        borderRadius: 'lg',
                        borderTopRightRadius: isSent ? 0 : 'lg',
                        borderTopLeftRadius: isSent ? 'lg' : 0,
                        color: isSent ? '#fff' : '#000',
                        backgroundColor: isSent
                            ? '#4164c1'
                            : 'background.body',
                    }}
                >
                    <Typography
                        level="body-sm"
                        sx={{
                            color: isSent
                                ? 'var(--joy-palette-common-white)'
                                : 'var(--joy-palette-text-primary)',
                        }}
                    >
                        {message.message}
                    </Typography>
                </Sheet>
            </Box>
        </Box>
    )
}

export default ChatMessageComponent;