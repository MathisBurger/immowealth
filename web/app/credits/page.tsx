'use client';
import {useGetCreditsQuery} from "@/generated/graphql";
import {Button, Divider, Grid, Table, Typography} from "@mui/joy";
import OpenInNewIcon from "@mui/icons-material/OpenInNew";
import {useRouter} from "next/navigation";

const CreditsPage = () => {

    const {data} = useGetCreditsQuery();
    const router = useRouter();

    return (
        <>
            <Typography level="h1">Kredite</Typography>
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
                    <th>Summe</th>
                    <th>Getilgt</th>
                    <th>Zins</th>
                    <th>Tilgung</th>
                    <th>Bank</th>
                    <th>Aktionen</th>
                </tr>
                </thead>
                <tbody>
                {data?.allCredits.map((credit: any) => (
                    <tr>
                        <td>{credit?.credit.id}</td>
                        <td>{credit?.credit.amount}</td>
                        <td>{credit?.creditRateSum}</td>
                        <td>{credit?.credit.interestRate}</td>
                        <td>{credit?.credit.redemptionRate}</td>
                        <td>{credit?.credit.bank}</td>
                        <td>
                            <Grid container direction="row">
                                <Grid xs={4}>
                                    <Button onClick={() => router.push('/credits/' + credit?.credit.id)}>
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
    );
}

export default CreditsPage;