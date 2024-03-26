import {Alert, Button, FormControl, FormLabel, Grid, Input, Modal, ModalDialog, Typography} from "@mui/joy";
import {useState} from "react";
import {GetObjectDocument, useAddCreditRateMutation} from "@/generated/graphql";
import LoadingButton from "@/components/LoadingButton";
import {DatePicker} from "@mui/x-date-pickers";
import dayjs from "dayjs";
import {useTranslation} from "next-export-i18n";

interface AddCreditRateModalProps {
    /**
     * Executed to close modal
     */
    onClose: () => void;
    /**
     * ID of credit
     */
    creditId: number;
    /**
     * ID of real estate
     */
    objectId: number|undefined;
}

/**
 * Modal to add a credit rate
 *
 * @constructor
 */
const AddCreditRateModal = ({onClose, creditId, objectId}: AddCreditRateModalProps) => {

    const [errorMessage, setErrorMessage] = useState<string|null>(null);
    const [rate, setRate] = useState<number>(0);
    const [date, setDate] = useState<Date>(new Date());
    const [note, setNote] = useState<string>('');
    const {t} = useTranslation();
    const [mutation, {loading}] = useAddCreditRateMutation({
        refetchQueries: objectId ? [
            {
                query: GetObjectDocument,
                variables: {id: objectId}
            }
        ] : []
    });

    const submit = async () => {
        if (rate === 0) {
            setErrorMessage(t('object.message.pleaseEnterValue'));
            return;
        }
        if (date.getTime() > (new Date()).getTime()) {
            setErrorMessage(t('object.message.noFutureDate'));
            return;
        }
        const result = await mutation({
            variables: {input: {id: creditId, date: new Date(), rate, note}}
        });
        if ((result.errors?.length ?? 0) > 0) {
            setErrorMessage((result?.errors ?? [])[0]?.message);
        } else {
            onClose();
        }
    }

    return (
        <Modal open onClose={onClose}>
            <ModalDialog variant="outlined">
                <Typography level="h2">
                    {t('credit.button.add-rate')}
                </Typography>
                {errorMessage !== null && (
                    <Alert color="warning" variant="soft">
                        {errorMessage}
                    </Alert>
                )}
                <DatePicker
                    label={t('credit.boookingDay')}
                    value={dayjs(date)}
                    disableFuture
                    onChange={(newDate) => setDate(newDate !== null ? newDate.toDate() : new Date())}
                />
                <FormControl>
                    <FormLabel>{t('credit.rate')}</FormLabel>
                    <Input type="number" value={rate} onChange={(e) => setRate(parseFloat(e.target.value))} />
                </FormControl>
                <FormControl>
                    <FormLabel>{t('credit.note')}</FormLabel>
                    <Input type="type" value={note} onChange={(e) => setNote(e.target.value)} />
                </FormControl>
                <Grid container direction="row" spacing={2} justifyContent="flex-end">
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
                        <LoadingButton variant="solid" color="primary" onClick={submit} loading={loading}>
                            {t('common.add')}
                        </LoadingButton>
                    </Grid>
                </Grid>
            </ModalDialog>
        </Modal>
    );
}

export default AddCreditRateModal;