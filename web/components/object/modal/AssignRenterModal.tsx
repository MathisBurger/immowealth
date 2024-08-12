import {Autocomplete, Button, Grid, Modal, ModalDialog, Typography} from "@mui/joy";
import {useTranslation} from "next-export-i18n";
import {useAssignRenterToObjectMutation, useGetUnassignedRentersQuery} from "@/generated/graphql";
import {FormEvent, useMemo, useState} from "react";
import LoadingButton from "@/components/LoadingButton";

interface AssignRenterModalProps {
    objectId: number;
    onClose: () => void;
    refetch: () => void;
}

interface AutocompleteType {
    renterId: number;
    label: string;
    firstLetter: string|undefined;
}

const AssignRenterModal = ({objectId, onClose, refetch}: AssignRenterModalProps) => {

    const {t} = useTranslation();
    const {data, loading} = useGetUnassignedRentersQuery();
    const [value, setValue] = useState<AutocompleteType|null>(null);
    const [assignMutation] = useAssignRenterToObjectMutation();

    const options = useMemo(() => {
        const raw =  data?.unassignedRenters?.map((r) => ({renterId: r?.id, label: `${r?.firstName} ${r?.lastName}`, firstLetter: r?.firstName?.charAt(0)})) ?? []
        return raw.sort((a, b) => -a.label.localeCompare(b.label))
    }, [data]);

    const onSubmit = async () => {
        const data = await assignMutation({
            variables: {renterId: value?.renterId ?? -1, objectId: objectId}
        });
        if (data.errors === undefined) {
            refetch();
            onClose();
        }
    }

    return (
        <Modal open onClose={onClose}>
            <ModalDialog variant="outlined">
                <Typography level="h2">
                    {t('object.renter.assignRenter')}
                </Typography>
                <Autocomplete
                    options={options}
                    loading={loading}
                    value={value}
                    groupBy={(o) => o.firstLetter ?? ''}
                    onChange={(_, e) => setValue(e)}
                />
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
                        <LoadingButton variant="solid" color="primary" onClick={onSubmit} loading={loading}>
                            {t('common.add')}
                        </LoadingButton>
                    </Grid>
                </Grid>
            </ModalDialog>
        </Modal>
    );
}

export default AssignRenterModal;