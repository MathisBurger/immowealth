'use client';
import {
    AllHousePriceChangesDocument,
    GetAllObjectsDocument,
    HousePriceChangeDataFragment,
    useAllHousePriceChangesQuery, useDeleteHousePriceChangeMutation,
    useDeleteRealEstateMutation
} from "@/generated/graphql";
import {useMemo, useState} from "react";
import {Divider, Select, Typography, Option, Table, Button, Autocomplete, Grid} from "@mui/joy";
import {useRouter} from "next/navigation";
import DeleteIcon from "@mui/icons-material/Delete";
import UpdateHousePriceModal from "@/components/housePriceChanges/UpdateHousePriceModal";
import EditIcon from "@mui/icons-material/Edit";
import {formatNumber} from "@/utilts/formatter";
import {GridColDef, GridValueFormatterParams} from "@mui/x-data-grid";
import EntityList from "@/components/EntityList";


const HousingPrices = () => {

    const {data} = useAllHousePriceChangesQuery();
    const router = useRouter();
    const [updateObject, setUpdateObject] = useState<HousePriceChangeDataFragment|null>(null);

    const [deleteMutation, {loading: deleteLoading}] = useDeleteHousePriceChangeMutation({
        refetchQueries: [
            {
                query: AllHousePriceChangesDocument
            }
        ]
    });

    const deleteObject = async (id: string) => {
        const result = await deleteMutation({
            variables: {id: parseInt(`${id}`)}
        });
        if (result.errors === undefined) {
            router.push('/objects');
        }
    }

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'id',
            headerName: 'ID'
        },
        {
            field: 'change',
            headerName: 'Änderung',
            width: 200,
            valueFormatter: ({value}: GridValueFormatterParams) => `${formatNumber(value)}€`
        },
        {
            field: 'zip',
            headerName: 'Postleitzahl'
        },
        {
            field: 'year',
            headerName: 'Jahr'
        },
        {
            field: 'actions',
            width: 400,
            headerName: 'Aktionen',
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
    ], []);

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
            <Typography level="h1">Preisänderungen</Typography>
            <Button
                variant="solid"
                color="primary"
                sx={{width: '200px'}}
                onClick={() => router.push("/housingPrices/new")}
            >
                Neue Preisänderung
            </Button>
            <Divider />
            <Autocomplete
                placeholder="Choose"
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