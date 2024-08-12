"use client";
import {
    GetObjectDocument,
    RenterFragment,
    useDeleteRenterFromObjectMutation,
    useUnassignRenterFromObjectMutation
} from "@/generated/graphql";
import EntityList from "@/components/EntityList";
import {useMemo, useState} from "react";
import {GridColDef, GridRenderCellParams} from "@mui/x-data-grid";
import {useTranslation} from "next-export-i18n";
import {Button, Card, CardContent, Grid, Stack} from "@mui/joy";
import CreateRenterModal from "@/components/object/modal/CreateRenterModal";
import OpenInNewIcon from "@mui/icons-material/OpenInNew";
import DeleteIcon from "@mui/icons-material/Delete";
import PersonRemoveIcon from '@mui/icons-material/PersonRemove';
import AssignRenterModal from "@/components/object/modal/AssignRenterModal";
import {useRouter} from "next/navigation";

interface ObjectRentersTabProps {
    renters: RenterFragment[];
    objectId: number;
    refetch: () => void;
}

const ObjectRentersTab = ({renters, objectId, refetch}: ObjectRentersTabProps) => {

    const {t} = useTranslation();
    const [modalOpen, setModalOpen] = useState<boolean>(false);
    const [assignModalOpen, setAssignModalOpen] = useState<boolean>(false);
    const [deleteMutation, {loading}] = useDeleteRenterFromObjectMutation({
        refetchQueries: [
            {
                query: GetObjectDocument,
                variables: {id: objectId}
            }
        ]
    });
    const router = useRouter();

    const [unassignMutation, {loading: unassignLoading}] = useUnassignRenterFromObjectMutation({
        refetchQueries: [
            {
                query: GetObjectDocument,
                variables: {id: objectId}
            }
        ]
    });

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'firstName',
            headerName: t('common.firstName')
        },
        {
            field: 'lastName',
            headerName: t('common.lastName')
        },
        {
            field: 'birthDay',
            headerName: t('common.birthday')
        },
        {
            field: '_actions',
            headerName: t('common.actions'),
            width: 300,
            renderCell: ({row}: GridRenderCellParams) => (
                <Grid container direction="row" spacing={2}>
                    {row.statistics && (
                        <Grid>
                            <Button onClick={() => router.push('/renter/details?id=' + row.id)} color="primary" loading={loading}>
                                <OpenInNewIcon />
                            </Button>
                        </Grid>
                    )}
                    <Grid>
                        <Button onClick={() => unassignMutation({variables: {renterId: row.id}})} color="warning" loading={unassignLoading}>
                            <PersonRemoveIcon />
                        </Button>
                    </Grid>
                    <Grid>
                        <Button onClick={() => deleteMutation({variables: {renterId: row.id}})} color="danger" loading={loading}>
                            <DeleteIcon />
                        </Button>
                    </Grid>
                </Grid>
            )
        }
    ], [t]);

    return (
        <>
            <Stack spacing={2}>
                <Card>
                    <CardContent>
                        <Grid container direction="row">
                            <Grid xs={2}>
                                <Button
                                    color="primary"
                                    variant="soft"
                                    onClick={() => setModalOpen(true)}
                                >{t('object.renter.createRenter')}</Button>
                            </Grid>
                            <Grid xs={2}>
                                <Button
                                    color="primary"
                                    variant="soft"
                                    onClick={() => setAssignModalOpen(true)}
                                >{t('object.renter.assignRenter')}</Button>
                            </Grid>
                        </Grid>
                    </CardContent>
                </Card>
                <EntityList
                    columns={cols}
                    rows={renters}
                />
            </Stack>
            {modalOpen && (
                <CreateRenterModal
                    onClose={() => setModalOpen(false)}
                    objectID={objectId}
                    refetch={refetch}
                />
            )}
            {assignModalOpen && (
                <AssignRenterModal
                    objectId={objectId}
                    onClose={() => setAssignModalOpen(false)}
                    refetch={refetch}
                />
            )}
        </>
    )
}

export default ObjectRentersTab;