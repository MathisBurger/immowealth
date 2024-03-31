import {
    AllHousePriceChangesDocument,
    HousePriceChangeDataFragment,
    useUpdateHousePriceChangeMutation
} from "@/generated/graphql";
import {Button, FormControl, FormLabel, Grid, Input, Modal, ModalDialog, Stack, Typography} from "@mui/joy";
import {FormEvent} from "react";
import {useTranslation} from "next-export-i18n";

interface UpdateHousePriceModalProps {
    /**
     * Executed to close modal
     */
    onClose: () => void;
    /**
     * The house price base data
     */
    housePrice: HousePriceChangeDataFragment;
}

const UpdateHousePriceModal = ({onClose, housePrice}: UpdateHousePriceModalProps) => {

    const [mutation, {loading}] = useUpdateHousePriceChangeMutation({
        refetchQueries: [
            {
                query: AllHousePriceChangesDocument
            }
        ]
    });

    const {t} = useTranslation();

    const submit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        let data = new FormData(e.currentTarget);
        const result = await mutation({
            variables: {input: {
                id: housePrice.id,
                    change: parseFloat(`${data.get('change')}`),
                    year: parseInt(`${data.get('year')}`, 10),
                    zip: `${data.get('zip')}`
                }}
        });
        if (result.errors === undefined) {
            onClose();
        }
    }

    return (
      <Modal open onClose={onClose}>
        <ModalDialog>
            <Typography level="h2">
                {t('common.update')}
            </Typography>
            <form onSubmit={submit}>
                <Stack spacing={2}>
                    <FormControl>
                        <FormLabel>{t('common.change')}</FormLabel>
                        <Input endDecorator="%" name="change" type="number" variant="soft" defaultValue={housePrice.change ?? 0}/>
                    </FormControl>
                    <FormControl>
                        <FormLabel>{t('common.year')}</FormLabel>
                        <Input name="year" type="number" variant="soft" defaultValue={housePrice.year ?? 0} />
                    </FormControl>
                    <FormControl>
                        <FormLabel>{t('common.zip')}</FormLabel>
                        <Input name="zip" type="text" variant="soft" defaultValue={housePrice.zip ?? ''} />
                    </FormControl>
                    <Grid container direction="row" spacing={2}>
                        <Grid>
                            <Button color="neutral" variant="soft" onClick={onClose}>
                                {t('common.cancel')}
                            </Button>
                        </Grid>
                        <Grid>
                            <Button color="primary" variant="solid" type="submit">
                                {t('common.save')}
                            </Button>
                        </Grid>
                    </Grid>
                </Stack>
            </form>
        </ModalDialog>
      </Modal>
    );
}

export default UpdateHousePriceModal;