'use client';
import {
    AllHousePriceChangesDocument,
    GetAllObjectsDocument,
    HousePriceChangeDataFragment,
    useAllHousePriceChangesQuery, useDeleteHousePriceChangeMutation,
    useDeleteRealEstateMutation
} from "@/generated/graphql";
import {useCallback, useMemo, useState} from "react";
import {Divider, Select, Typography, Option, Table, Button, Autocomplete, Grid} from "@mui/joy";
import {useRouter} from "next/navigation";
import DeleteIcon from "@mui/icons-material/Delete";
import UpdateHousePriceModal from "@/components/housePriceChanges/UpdateHousePriceModal";
import EditIcon from "@mui/icons-material/Edit";
import {formatNumber} from "@/utilts/formatter";
import {GridColDef, GridValueFormatterParams} from "@mui/x-data-grid";
import EntityList from "@/components/EntityList";
import {useTranslation} from "next-export-i18n";


const HousingPrices = () => {

    const {data} = useAllHousePriceChangesQuery();
    const router = useRouter();
    const {t} = useTranslation();
    const [updateObject, setUpdateObject] = useState<HousePriceChangeDataFragment|null>(null);

    const [deleteMutation, {loading: deleteLoading}] = useDeleteHousePriceChangeMutation({
        refetchQueries: [
            {
                query: AllHousePriceChangesDocument
            }
        ]
    });

    const deleteObject = useCallback(async (id: string) => {
        const result = await deleteMutation({
            variables: {id: parseInt(`${id}`)}
        });
        if (result.errors === undefined) {
            router.push('/objects');
        }
    }, [router, deleteMutation]);

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'id',
            headerName: 'ID'
        },
        {
            field: 'change',
            headerName: t('common.change'),
            width: 200,
            valueFormatter: ({value}: GridValueFormatterParams) => `${formatNumber(value)}€`
        },
        {
            field: 'zip',
            headerName: t('common.zip')
        },
        {
            field: 'year',
            headerName: t('common.year')
        },
        {
            field: 'actions',
            width: 400,
            headerName: t('common.actions'),
            renderCell: ({row}) => (
                <Grid container direction="row" spacing={2}>
                    <Grid xs={6}>
                        <Button color="primary" onClick={() => setUpdateObject(row)}>
                            <EditIcon />
                        </Button>
                    </Grid>
                    <Grid xs={6}>
                        <Button color="danger" onClick={() => deleteObject(`${row.id}`)} loading={deleteLoading}>
                            <DeleteIcon />
                        </Button>
                    </Grid>
                </Grid>
            )
        }
    ], [deleteLoading, deleteObject]);

    const [filter, setFilter] = useState<string>('');

    const zips = useMemo<string[]>(() => {
        let all: string[] = [];
        for (let el of data?.allHousePricesChange ?? []) {
            if (!all.includes(el?.zip ?? '')) {
                all.push(el?.zip ?? '');
            }
        }
        return all;
    }, [data]);

    const filtered = useMemo<HousePriceChangeDataFragment[]>(
        () => (data?.allHousePricesChange ?? [])
            .filter((el) => filter === '' || (el?.zip ?? '').startsWith(filter)) as HousePriceChangeDataFragment[],
    [filter, data]);

    return (
        <>
            <Typography level="h1">{t('housingPrices.housingPricesChange')}</Typography>
            <Button
                variant="solid"
                color="primary"
                sx={{width: '200px'}}
                onClick={() => router.push("/housingPrices/new")}
            >
                {t('housingPrices.new')}
            </Button>
            <Divider />
            <Autocomplete
                placeholder={t('common.choose')}
                variant="soft"
                sx={{width: '200px'}}
                options={zips.map((o) => ({label: o, id: o}))}
                inputValue={filter}
                onInputChange={(_, f) => setFilter(f ?? '')}
            />
            <EntityList columns={cols} rows={filtered} />
            {updateObject && (
                <UpdateHousePriceModal
                    onClose={() => setUpdateObject(null)}
                    housePrice={updateObject}
                />
            )}
        </>
    );
}

export const dynamic = 'force-static';
export const dynamicParams = true;

export default HousingPrices;