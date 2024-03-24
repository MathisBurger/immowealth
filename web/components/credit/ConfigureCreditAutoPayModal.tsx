import {
    AutoPayInterval,
    CreditDataFragment,
    GetCreditDocument,
    GetObjectDocument,
    useConfigureCreditAutoPayMutation
} from "@/generated/graphql";
import {
    Button,
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
import dayjs from "dayjs";
import {useTranslation} from "next-export-i18n";

interface ConfigureCreditAutoPayModalProps {
    /**
     * Credit element
     */
    credit: CreditDataFragment|undefined;
    /**
     * Executed on modal close
     */
    onClose: () => void;
    /**
     * ID of element that should be refetched
     */
    refetchId: number;
    /**
     * Indicates a real estate refetch
     */
    isObjectRefetch?: boolean;
}

/**
 * Modal for auto pay configuration
 *
 * @constructor
 */
const ConfigureCreditAutoPayModal = ({credit, onClose, refetchId, isObjectRefetch}: ConfigureCreditAutoPayModalProps) => {

    const [mutation, {loading}] = useConfigureCreditAutoPayMutation({
        refetchQueries: isObjectRefetch ? [
            {query: GetObjectDocument, variables: {id: refetchId}}
        ] : [
            {query: GetCreditDocument, variables: {id: refetchId}}
        ]
    });

    const {t} = useTranslation();

    const [checked, setChecked] = useState<boolean>(credit?.autoPayInterval !== null && credit?.autoPayInterval !== undefined);

    const submit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);
        const result = await mutation({
            variables: {
                id: credit?.id ?? -1,
                enabled: checked,
                interval: formData.get("intervalType") as AutoPayInterval,
                amount: parseFloat(`${formData.get("amount")}`)
            }
        });
        if (result.errors === undefined) {
            onClose();
        }
    };

    return (
        <Modal open onClose={onClose}>
            <ModalDialog variant="outlined">
                <Typography level="h2">
                    {t('credit.button.configure-auto-booking')}
                </Typography>
                <form onSubmit={submit}>
                    <Grid container direction="row" spacing={2} justifyContent="flex-end">
                        <Grid xs={12}>
                            <Stack spacing={2}>
                                {credit?.nextCreditRate && checked && (
                                    <Typography level="h4">
                                        {t('credit.nextBooking')}: {dayjs(`${credit.nextCreditRate}`).format("DD.MM.YYYY")}
                                    </Typography>
                                )}
                                <Typography component="label" endDecorator={
                                    <Switch
                                        variant="solid"
                                        checked={checked}
                                        onChange={(event) => setChecked(event.target.checked)}
                                    />
                                }>
                                    {t('common.activated')}
                                </Typography>
                                <FormControl>
                                    <FormLabel>Interval</FormLabel>
                                    <Select name="intervalType" defaultValue={credit?.autoPayInterval} disabled={!checked}>
                                        {Object.entries(AutoPayInterval).map((el) => (
                                            <Option value={el[1]} key={el[1]}>{el[1]}</Option>
                                        ))}
                                    </Select>
                                </FormControl>
                                <FormControl>
                                    <FormLabel>{t('credit.bookingSum')}</FormLabel>
                                    <Input
                                        type="number"
                                        variant="outlined"
                                        endDecorator="€"
                                        name="amount"
                                        disabled={!checked}
                                        defaultValue={credit?.autoPayAmount ?? 0}
                                    />
                                </FormControl>
                            </Stack>
                        </Grid>
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
                            <LoadingButton variant="solid" color="primary" type="submit" loading={loading}>
                                {t('common.save')}
                            </LoadingButton>
                        </Grid>
                    </Grid>
                </form>
            </ModalDialog>
        </Modal>
    );
}

export default ConfigureCreditAutoPayModal;