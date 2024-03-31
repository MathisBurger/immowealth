import {Button, Grid, Modal, ModalDialog, Typography} from "@mui/joy";
import {useTranslation} from "next-export-i18n";
import {GetObjectDocument, useSetAutoBookingByExpensesMutation} from "@/generated/graphql";
import useSnackbar from "@/hooks/useSnackbar";

interface RentExpenseAutoBookingModalProps {
    objectId: number;
    onClose: () => void;
}

const RentExpenseAutoBookingModal = ({objectId, onClose}: RentExpenseAutoBookingModalProps) => {

    const {t} = useTranslation();
    const snackbar = useSnackbar();
    const [mutation, {loading}] = useSetAutoBookingByExpensesMutation({
        refetchQueries: [
            {
                query: GetObjectDocument,
                variables: {id: objectId}
            }
        ]
    });

    const onSubmit = async () => {
        const resp = await mutation({
            variables: {id: objectId}
        });
        if (resp.errors === undefined) {
            snackbar.success(t('object.expense.updated-auto-booking'));
            onClose();
        }
    }

    return (
        <Modal open onClose={onClose}>
            <ModalDialog>
                <Typography level="h2">
                    {t('credit.button.auto-booking')}
                </Typography>
                <Typography>
                    {t('object.expense.configure-auto-booking')}
                </Typography>
                <Grid container direction="row" spacing={2} justifyContent="flex-end">
                    <Grid>
                        <Button
                            variant="outlined"
                            color="neutral"
                            onClick={onClose}
                        >
                            {t('common.no')}
                        </Button>
                    </Grid>
                    <Grid>
                        <Button variant="solid" color="primary"loading={loading} onClick={onSubmit}>
                            {t('common.yes')}
                        </Button>
                    </Grid>
                </Grid>
            </ModalDialog>
        </Modal>
    );
}

export default RentExpenseAutoBookingModal;