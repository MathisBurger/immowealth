'use client';
import {Button, Divider, Grid, IconButton, Table, Typography} from "@mui/joy";
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
import {GetAllObjectsDocument, useDeleteRealEstateMutation, useGetAllObjectsQuery} from "@/generated/graphql";
import {useRouter} from "next/navigation";
import DeleteIcon from '@mui/icons-material/Delete';
import {formatNumber} from "@/utilts/formatter";
import EntityList from "@/components/EntityList";
import {useCallback, useMemo} from "react";
import {GridColDef, GridRenderCellParams, GridValueFormatterParams} from "@mui/x-data-grid";
import {useTranslation} from "next-export-i18n";
import useCurrencySymbol from "@/hooks/useCurrencySymbol";


const ObjectsPage = () => {

    const {data} = useGetAllObjectsQuery();
    const router = useRouter();
    const {t} = useTranslation();
    const currency = useCurrencySymbol();

    const [deleteMutation, {loading: deleteLoading}] = useDeleteRealEstateMutation({
        refetchQueries: [
            {
                query: GetAllObjectsDocument
            }
        ]
    });

    const deleteObject = useCallback(async (id: string) => {
        await deleteMutation({
            variables: {id: parseInt(`${id}`)}
        });
    }, [deleteMutation]);

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'id',
            headerName: 'ID'
        },
        {
            field: 'initialValue',
            headerName: t('object.buyPrice'),
            width: 200,
            valueFormatter: (value) => `${formatNumber(value)}${currency}`
        },
        {
            field: 'streetAndHouseNr',
            headerName: t('object.streetAndHouseNr'),
            width: 300
        },
        {
            field: 'zip',
            headerName: t('common.zip'),
        },
        {
            field: 'city',
            headerName: t('common.city'),
            width: 150
        },
        {
            field: 'dateBought',
            headerName: t('object.buyDate'),
        },
        {
            field: 'actions',
            headerName: t('common.actions'),
            width: 400,
            renderCell: ({row}: GridRenderCellParams) => (
                <Grid container direction="row" spacing={2}>
                    <Grid>
                        <Button onClick={() => router.push('/objects/details?id=' + row.id)}>
                            <OpenInNewIcon />
                        </Button>
                    </Grid>
                    <Grid>
                        <Button onClick={() => deleteObject(`${row.id}`)} color="danger" loading={deleteLoading}>
                            <DeleteIcon />
                        </Button>
                    </Grid>
                </Grid>
            )
        }
    ], [deleteLoading, router, deleteObject, t, currency]);

    return (
        <>
            <Typography level="h1">{t('common.objects')}</Typography>
            <Button
                variant="solid"
                color="primary"
                sx={{width: '200px'}}
                onClick={() => router.push("/objects/new")}
            >
                {t('object.new')}
            </Button>
            <Divider />
            <EntityList
                rows={data?.allObjects ?? []}
                columns={cols}
            />
        </>
    )
}

export const dynamic = 'force-static';
export const dynamicParams = true;

export default ObjectsPage;