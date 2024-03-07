'use client';
import {Button, Divider, Grid, IconButton, Table, Typography} from "@mui/joy";
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
import {GetAllObjectsDocument, useDeleteRealEstateMutation, useGetAllObjectsQuery} from "@/generated/graphql";
import {useRouter} from "next/navigation";
import DeleteIcon from '@mui/icons-material/Delete';


const ObjectsPage = () => {

    const {data} = useGetAllObjectsQuery();
    const router = useRouter();

    const [deleteMutation, {loading: deleteLoading}] = useDeleteRealEstateMutation({
        refetchQueries: [
            {
                query: GetAllObjectsDocument
            }
        ]
    });

    const deleteObject = async (id: string) => {
        await deleteMutation({
            variables: {id: parseInt(`${id}`)}
        });
    }

    return (
        <>
            <Typography level="h1">Objekte</Typography>
            <Button
                variant="solid"
                color="primary"
                sx={{width: '200px'}}
                onClick={() => router.push("/objects/new")}
            >
                Neues Objekt
            </Button>
            <Divider />
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
                    <th>Kaufpreis</th>
                    <th>Straße und Hausnummer</th>
                    <th>Postleitzahl</th>
                    <th>Ort</th>
                    <th>Kaufdatum</th>
                    <th>Aktionen</th>
                </tr>
               </thead>
                <tbody>
                {data?.allObjects.map((object) => (
                    <tr key={object?.id}>
                        <td>{object?.id}</td>
                        <td>{object?.initialValue}</td>
                        <td>{object?.streetAndHouseNr}</td>
                        <td>{object?.zip}</td>
                        <td>{object?.city}</td>
                        <td>{(new Date(object?.dateBought)).getUTCDay()}.{(new Date(object?.dateBought)).getUTCMonth()+1}.{(new Date(object?.dateBought)).getFullYear()}</td>
                        <td>
                            <Grid container direction="row" spacing={2}>
                                <Grid xs={5}>
                                    <Button onClick={() => router.push('/objects/details?id=' + object?.id)}>
                                        <OpenInNewIcon />
                                    </Button>
                                </Grid>
                                <Grid xs={5}>
                                    <Button onClick={() => deleteObject(`${object?.id}`)} color="danger" loading={deleteLoading}>
                                        <DeleteIcon />
                                    </Button>
                                </Grid>
                            </Grid>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </>
    )
}

export const dynamic = 'force-static';
export const dynamicParams = true;

export default ObjectsPage;