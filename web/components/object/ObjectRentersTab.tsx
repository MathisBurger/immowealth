"use client";
import {RenterFragment} from "@/generated/graphql";
import EntityList from "@/components/EntityList";
import {useMemo, useState} from "react";
import {GridColDef} from "@mui/x-data-grid";
import {useTranslation} from "next-export-i18n";
import {Button, Card, CardContent, Grid, Stack} from "@mui/joy";
import CreateRenterModal from "@/components/object/modal/CreateRenterModal";

interface ObjectRentersTabProps {
    renters: RenterFragment[];
    objectId: number;
    refetch: () => void;
}

const ObjectRentersTab = ({renters, objectId, refetch}: ObjectRentersTabProps) => {

    const {t} = useTranslation();
    const [modalOpen, setModalOpen] = useState<boolean>(false);

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
        }
    ], [t]);

    return (
        <>
            <Stack spacing={2}>
                <Card>
                    <CardContent>
                        <Grid container direction="row">
                            <Grid xs={3}>
                                <Button
                                    color="primary"
                                    variant="soft"
                                    onClick={() => setModalOpen(true)}
                                >{t('object.renter.createRenter')}</Button>
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
        </>
    )
}

export default ObjectRentersTab;