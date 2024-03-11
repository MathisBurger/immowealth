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
            <Table
                borderAxis="x"
                size="lg"
                stickyHeader
                stripe="even"
                variant="soft"
            >
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Änderung</th>
                    <th>Zip</th>
                    <th>Year</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {filtered.map((change) => (
                    <tr key={change.id}>
                        <td>{change.id}</td>
                        <td>{change.change}%</td>
                        <td>{change.zip}</td>
                        <td>{change.year}</td>
                        <td>
                            <Grid container direction="row">
                                <Grid xs={6}>
                                    <Button color="primary" onClick={() => setUpdateObject(change)}>
                                        <EditIcon />
                                    </Button>
                                </Grid>
                                <Grid xs={6}>
                                    <Button color="danger" onClick={() => deleteObject(`${change.id}`)} loading={deleteLoading}>
                                        <DeleteIcon />
                                    </Button>
                                </Grid>
                            </Grid>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
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