'use client';
import {Button, Divider, Grid, IconButton, Table, Typography} from "@mui/joy";
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
import {useGetAllObjectsQuery} from "@/generated/graphql";
import {useRouter} from "next/navigation";


const ObjectsPage = () => {

    const {data} = useGetAllObjectsQuery();
    const router = useRouter();

    return (
        <>
            <Typography level="h1">Objekte</Typography>
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
                    <tr>
                        <td>{object?.id}</td>
                        <td>{object?.initialValue}</td>
                        <td>{object?.streetAndHouseNr}</td>
                        <td>{object?.zip}</td>
                        <td>{object?.city}</td>
                        <td>{(new Date(object?.dateBought)).getUTCDay()}.{(new Date(object?.dateBought)).getUTCMonth()+1}.{(new Date(object?.dateBought)).getFullYear()}</td>
                        <td>
                            <Grid container direction="row">
                                <Grid xs={4}>
                                    <Button onClick={() => router.push('/objects/' + object?.id)}>
                                        <OpenInNewIcon />
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

export default ObjectsPage;