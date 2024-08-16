import {Button, FormControl, FormLabel, Grid, Input, Modal, ModalDialog, Typography} from "@mui/joy";
import {DatePicker} from "@mui/x-date-pickers";
import {useTranslation} from "next-export-i18n";
import {GetObjectDocument, useCreateRenterOnObjectMutation} from "@/generated/graphql";
import {FormEvent} from "react";

interface CreateRenterModalProps {
    onClose: () => void;
    refetch: () => void;
    objectID: number;
}

const CreateRenterModal = ({onClose, refetch, objectID}: CreateRenterModalProps) => {

    const {t} = useTranslation();
    const [mutation, {error}] = useCreateRenterOnObjectMutation({
        refetchQueries: [
            {
                query: GetObjectDocument,
                variables: {objectID}
            }
        ]
    });

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        const resp = await mutation({
            variables: {
                input: {
                    objectID: objectID,
                    firstName: `${formData.get("firstName")}`,
                    lastName: `${formData.get("lastName")}`,
                    birthDay: new Date(`${formData.get("birthDay")}`),
                    username: `${formData.get("username")}`,
                    password: `${formData.get("password")}`,
                    email: `${formData.get("email")}`
                }
            }
        });
        if (resp.errors === undefined) {
            refetch();
            onClose();
        }
    }


    return (
        <Modal open onClose={onClose}>
            <ModalDialog variant="outlined">
                <form onSubmit={onSubmit}>
                    <Typography level="h2">
                        {t('credit.button.add-rate')}
                    </Typography>
                    <FormControl>
                        <FormLabel>{t('common.firstName')}</FormLabel>
                        <Input type="text" name="firstName" />
                    </FormControl>
                    <FormControl>
                        <FormLabel>{t('common.lastName')}</FormLabel>
                        <Input type="text" name="lastName" />
                    </FormControl>
                    <FormControl>
                        <FormLabel>{t('common.birthday')}</FormLabel>
                        <DatePicker
                            label={t('common.birthday')}
                            disableFuture
                            name="birthDay"
                        />
                    </FormControl>
                    <FormControl>
                        <FormLabel>{t('common.username')}</FormLabel>
                        <Input type="text" name="username" />
                    </FormControl>
                    <FormControl>
                        <FormLabel>{t('common.password')}</FormLabel>
                        <Input type="password" name="password" />
                    </FormControl>
                    <FormControl>
                        <FormLabel>{t('common.email')}</FormLabel>
                        <Input type="email" name="email" />
                    </FormControl>

                    <Grid container direction="row" spacing={2} justifyContent="flex-end" sx={{marginTop: '10px'}}>
                        <Grid>
                            <Button
                                variant="outlined"
                                color="neutral"
                                onClick={onClose}
                            >
                                {t('common.cancel')}
                            </Button>
                        </Grid>
                        <Grid>
                            <Button variant="solid" color="primary" type="submit">
                                {t('common.add')}
                            </Button>
                        </Grid>
                    </Grid>
                </form>
            </ModalDialog>
        </Modal>
    );
}

export default CreateRenterModal;