'use client';
import {GetObjectQuery} from "@/generated/graphql";
import {Card, CardContent, Grid, Table} from "@mui/joy";
import {LineChart} from "@mui/x-charts";
import LoadingSpinner from "@/components/LoadingSpinner";

interface ObjectPriceChangesTabProps {
    loading: boolean;
    data: GetObjectQuery|undefined;
}

const ObjectPriceChangesTab = ({loading, data}: ObjectPriceChangesTabProps) => {

    return (
        <LoadingSpinner loading={loading}>
            <Grid container direction="row" spacing={2}>
                <Grid xs={6}>
                    <Card>
                        <CardContent>
                            <Table
                                borderAxis="x"
                                size="lg"
                                stickyHeader
                                stripe="even"
                                variant="soft"
                            >
                                <thead>
                                <tr>
                                    <th>Preis</th>
                                    <th>Jahr</th>
                                </tr>
                                </thead>
                                <tbody>
                                {data?.object.priceChanges.map((object) => (
                                    <tr>
                                        <td>{object?.value}€</td>
                                        <td>{object?.year}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </Table>
                        </CardContent>
                    </Card>
                </Grid>
                <Grid xs={6}>
                    <Card>
                        <CardContent>
                            <LineChart
                                series={[{data: data?.object.priceChanges.map((c) => c?.value ?? 0)}]}
                                xAxis={[{data: data?.object.priceChanges.map((c) => c?.year ?? 0)}]}
                                height={500}
                            />
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
        </LoadingSpinner>
    );
}

export default ObjectPriceChangesTab;