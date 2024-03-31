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
    Stack,
    Typography
} from "@mui/joy";
import {useTranslation} from "next-export-i18n";
import useCurrencySymbol from "@/hooks/useCurrencySymbol";
import {
    GetObjectDocument,
    ObjectRentType, RentExpenseDataFragment,
    useAddRentExpenseMutation,
    useUpdateRentExpenseMutation
} from "@/generated/graphql";
import {FormEvent, useState} from "react";
import RentExpenseAutoBookingModal from "@/components/object/modal/RentExpenseAutoBookingModal";

interface AddRentExpenseModalProps {
    objectId: number;
    /**
     * Executed to close modal
     */
    onClose: () => void;
    expense?: RentExpenseDataFragment;
}

const RentExpenseActionModal = ({objectId, onClose, expense}: AddRentExpenseModalProps) => {

    const {t} = useTranslation();
    const currency = useCurrencySymbol();
    const [autoBookingDialog, setAutoBookingDialog] = useState<boolean>(false);

    const [addMutation, {loading: addLoading}] = useAddRentExpenseMutation({
        refetchQueries: [
            {
                query: GetObjectDocument,
                variables: {id: objectId}
            }
        ]
    });

    const [updateMutation, {loading: updateLoading}] = useUpdateRentExpenseMutation({
        refetchQueries: [
            {
                query: GetObjectDocument,
                variables: {id: objectId}
            }
        ]
    });

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const data = new FormData(e.currentTarget);
        let errors = undefined;
        if (expense) {
            const err = await updateMutation({
                variables: {
                    id: expense.id,
                    value: parseFloat(`${data.get('value')}`),
                    name: `${data.get('name')}`,
                    type: `${data.get('type')}` as ObjectRentType
                }
            });
            errors = err.errors;
        } else {
            const err = await addMutation({
                variables: {
                    objectId: objectId,
                    value: parseFloat(`${data.get('value')}`),
                    name: `${data.get('name')}`,
                    type: `${data.get('type')}` as ObjectRentType
                }
            });
            errors = err.errors;
        }
        if (errors === undefined) {
            setAutoBookingDialog(true);
        }
    }

    return (
        <>
            <Modal open onClose={onClose}>
                <ModalDialog variant="outlined">
                    <Typography level="h2">
                        {t('object.expense.add')}
                    </Typography>
                    <form onSubmit={onSubmit}>
                        <Stack spacing={2}>
                            <FormControl>
                                <FormLabel>{t('object.expense.name')}</FormLabel>
                                <Input type="text" variant="soft" name="name" defaultValue={expense ? expense.name! : undefined} />
                            </FormControl>
                            <FormControl>
                                <FormLabel>{t('object.expense.value')}</FormLabel>
                                <Input type="number" variant="soft" name="value" endDecorator={currency} defaultValue={expense ? expense.value! : undefined} />
                            </FormControl>
                            <FormControl>
                                <FormLabel>{t('object.expense.type')}</FormLabel>
                                <Select variant="soft" name="type" defaultValue={expense ? expense.type! : undefined}>
                                    {Object.values(ObjectRentType).map((type) => (
                                        <Option value={type} key={type}>{t(`object.expense.types.${type}`)}</Option>
                                    ))}
                                </Select>
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
                                    <Button variant="solid" color="primary"loading={addLoading || updateLoading} type="submit">
                                        {expense ? t('common.save') : t('common.add')}
                                    </Button>
                                </Grid>
                            </Grid>
                        </Stack>
                    </form>
                </ModalDialog>
            </Modal>
            {autoBookingDialog && (
                <RentExpenseAutoBookingModal
                    objectId={objectId}
                    onClose={() => {
                        setAutoBookingDialog(false);
                        onClose();
                    }}
                />
            )}
        </>
    )
}

export default RentExpenseActionModal;