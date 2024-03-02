import {Alert, Button, Grid, Input, Modal, ModalDialog, Typography} from "@mui/joy";
import {useState} from "react";
import {GetObjectDocument, useAddCreditRateMutation} from "@/generated/graphql";
import LoadingButton from "@/components/LoadingButton";
import {DatePicker} from "@mui/x-date-pickers";
import dayjs from "dayjs";

interface AddCreditRateModalProps {
    onClose: () => void;
    creditId: number;
    objectId: number|undefined;
}

const AddCreditRateModal = ({onClose, creditId, objectId}: AddCreditRateModalProps) => {

    const [errorMessage, setErrorMessage] = useState<string|null>(null);
    const [rate, setRate] = useState<number>(0);
    const [date, setDate] = useState<Date>(new Date());
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
            setErrorMessage('Bitte gebe einen Wert ein.');
            return;
        }
        if (date.getTime() > (new Date()).getTime()) {
            setErrorMessage('Das Datum darf nicht in der Zukunft liegen');
            return;
        }
        const result = await mutation({
            variables: {input: {id: creditId, date: new Date(), rate}}
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
                    Kreditrate hinzufügen
                </Typography>
                {errorMessage !== null && (
                    <Alert color="warning" variant="soft">
                        {errorMessage}
                    </Alert>
                )}
                <DatePicker
                    label="Buchungstag"
                    value={dayjs(date)}
                    disableFuture
                    onChange={(newDate) => setDate(newDate !== null ? newDate.toDate() : new Date())}
                />
                <Input type="number" value={rate} onChange={(e) => setRate(parseFloat(e.target.value))} />
                <Grid container direction="row" spacing={2} justifyContent="flex-end">
                    <Grid>
                        <Button
                            variant="outlined"
                            color="neutral"
                            onClick={onClose}
                        >
                            Abbrechen
                        </Button>
                    </Grid>
                    <Grid>
                        <LoadingButton variant="solid" color="primary" onClick={submit} loading={loading}>
                            Hinzufügen
                        </LoadingButton>
                    </Grid>
                </Grid>
            </ModalDialog>
        </Modal>
    );
}

export default AddCreditRateModal;