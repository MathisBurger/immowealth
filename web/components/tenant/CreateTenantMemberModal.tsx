'use client';
import {
    Button,
    ButtonGroup,
    FormControl,
    FormLabel,
    Input,
    Modal,
    ModalDialog,
    Stack,
    TextField,
    Typography
} from "@mui/joy";
import {useTranslation} from "next-export-i18n";
import {GetTenantDocument, useCreateUserMutation} from "@/generated/graphql";
import {FormEvent} from "react";
import UserRoles from "@/utilts/userRoles";


interface CreateTenantMemberModalProps {
    tenantId: number;
    onClose: () => void;
}

const CreateTenantMemberModal = ({tenantId, onClose}: CreateTenantMemberModalProps) => {

    const {t} = useTranslation();
    const [createTenantMemberMutation] = useCreateUserMutation({
        refetchQueries: [
            {
                query: GetTenantDocument,
                variables: {id: tenantId}
            }
        ]
    });

    const createTenantMember = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const data = new FormData(e.currentTarget);
        const {errors} = await createTenantMemberMutation({
            variables: {
                input: {
                    username: `${data.get('username')}`,
                    email: `${data.get('email')}`,
                    password: `${data.get('password')}`,
                    roles: [UserRoles.TENANT_ASSIGNED],
                    tenantId: tenantId,
                }
            }
        });
        if (errors === undefined) {
            onClose();
        }
    }

    return (
        <Modal open onClose={onClose}>
            <ModalDialog>
                <Typography level="h1">{t('tenant.createMember')}</Typography>
                <form onSubmit={createTenantMember}>
                    <Stack spacing={2}>
                        <FormControl>
                            <FormLabel>{t('common.username')}</FormLabel>
                            <Input
                                name="username"
                                type="text"
                                required
                            />
                        </FormControl>
                        <FormControl>
                            <FormLabel>{t('common.email')}</FormLabel>
                            <Input
                                name="email"
                                type="email"
                                required
                            />
                        </FormControl>
                        <FormControl>
                            <FormLabel>{t('common.password')}</FormLabel>
                            <Input
                                name="password"
                                type="password"
                                required
                            />
                        </FormControl>
                        <ButtonGroup>
                            <Button type="submit" color="primary" variant="soft">{t('common.create')}</Button>
                            <Button color="neutral" variant="soft">{t('common.cancel')}</Button>
                        </ButtonGroup>
                    </Stack>
                </form>
            </ModalDialog>
        </Modal>
    );
}

export default CreateTenantMemberModal;