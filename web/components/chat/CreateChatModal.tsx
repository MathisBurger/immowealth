import {Autocomplete, Button, Modal, ModalDialog, Stack} from "@mui/joy";
import {GetAllChatsDocument, useCreateChatWithUserMutation, useGetAllUsersQuery} from "@/generated/graphql";
import {FormEvent, useMemo, useState} from "react";
import {useTranslation} from "next-export-i18n";

interface CreateChatModalProps {
    onClose: () => void;
}

const CreateChatModal = ({onClose}: CreateChatModalProps) => {

    const {data} = useGetAllUsersQuery();
    const [value, setValue] = useState<any>(null);
    const [mutation] = useCreateChatWithUserMutation({
        refetchQueries: [
            {
                query: GetAllChatsDocument
            }
        ]
    })
    const {t} = useTranslation();

    const options = useMemo(() => {
        return (data?.allUsers ?? []).map((u) => ({title: u?.username, id: u?.id}));
    }, [data]);

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (value !== null && value !== undefined) {
            const res = await mutation({
                variables: {userId: value.id}
            });
            if (res.errors === undefined) {
                onClose();
            }
        }

    }


    return (
        <Modal open onClose={onClose}>
            <ModalDialog>
                <form onSubmit={onSubmit}>
                    <Stack spacing={2}>
                        <Autocomplete
                            options={options}
                            value={value}
                            onChange={(_, v) => setValue(v)}
                            getOptionKey={(o) => o.id!}
                            getOptionLabel={(o) => o.title!}
                            required
                        />
                        <Button color="primary" type="submit">{t('common.create')}</Button>
                    </Stack>
                </form>
            </ModalDialog>
        </Modal>
    );
}

export default CreateChatModal;