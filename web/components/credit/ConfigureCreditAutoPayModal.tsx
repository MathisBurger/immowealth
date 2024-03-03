import {
    AutoPayInterval,
    CreditDataFragment,
    GetCreditDocument,
    GetObjectDocument,
    useConfigureCreditAutoPayMutation
} from "@/generated/graphql";
import {
    Button,
    Checkbox,
    FormControl,
    FormLabel,
    Grid,
    Input,
    Modal,
    ModalDialog,
    Option,
    Select,
    Stack, Switch,
    Typography
} from "@mui/joy";
import LoadingButton from "@/components/LoadingButton";
import {FormEvent, useState} from "react";

interface ConfigureCreditAutoPayModalProps {
    credit: CreditDataFragment|undefined;
    onClose: () => void;
    refetchId: number;
    isObjectRefetch?: boolean;
}

const ConfigureCreditAutoPayModal = ({credit, onClose, refetchId, isObjectRefetch}: ConfigureCreditAutoPayModalProps) => {

    const [mutation, {loading}] = useConfigureCreditAutoPayMutation({
        refetchQueries: isObjectRefetch ? [
            {query: GetObjectDocument, variables: {id: refetchId}}
        ] : [
            {query: GetCreditDocument, variables: {id: refetchId}}
        ]
    });

    const [checked, setChecked] = useState<boolean>(credit?.autoPayInterval !== null && credit?.autoPayInterval !== undefined);

    const submit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);
        const result = await mutation({
            variables: {id: credit?.id ?? -1, enabled: checked, interval: formData.get("intervalType") as AutoPayInterval}
        });
        if (result.errors === undefined) {
            onClose();
        }
    };

    return (
        <Modal open onClose={onClose}>
            <ModalDialog variant="outlined">
                <Typography level="h2">
                    Automatische Buchung konfigurieren
                </Typography>
                <form onSubmit={submit}>
                    <Grid container direction="row" spacing={2} justifyContent="flex-end">
                        <Grid xs={12}>
                            <Stack spacing={2}>
                                <Typography component="label" endDecorator={
                                    <Switch
                                        variant="solid"
                                        checked={checked}
                                        onChange={(event) => setChecked(event.target.checked)}
                                    />
                                }>
                                    Aktiviert
                                </Typography>
                                <FormControl>
                                    <FormLabel>Interval</FormLabel>
                                    <Select name="intervalType" defaultValue={credit?.autoPayInterval}>
                                        {Object.entries(AutoPayInterval).map((el) => (
                                            <Option value={el[1]}>{el[1]}</Option>
                                        ))}
                                    </Select>
                                </FormControl>
                            </Stack>
                        </Grid>
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
                            <LoadingButton variant="solid" color="primary" type="submit" loading={loading}>
                                Speichern
                            </LoadingButton>
                        </Grid>
                    </Grid>
                </form>
            </ModalDialog>
        </Modal>
    );
}

export default ConfigureCreditAutoPayModal;