import {Card, CardContent, Grid, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";
import {BarChart, BarSeriesType} from "@mui/x-charts";
import {GetObjectQuery} from "@/generated/graphql";
import {useMemo} from "react";

interface ObjectCreditChartCardProps {
    loading: boolean;
    data: GetObjectQuery|undefined;
}

type BarChartData = Omit<BarSeriesType, 'type'>;

const ObjectCreditChartCard = ({loading, data}: ObjectCreditChartCardProps) => {

    const series = useMemo<BarChartData[]>(() => [
        {dataKey: 'paid', label: 'Getilgt'},
        {dataKey: 'total', label: 'Kredit'},
    ], []);

    return (
        <Card variant="outlined">
            <CardContent>
                <LoadingSpinner loading={loading}>
                    <Typography level="h3">Finanzierung</Typography>
                    <Grid container direction="row">
                        <Grid xs={4}>
                            <BarChart series={series} height={400} dataset={[
                                {
                                    paid: data?.object.creditRateSum ?? 0,
                                    total: data?.object.realEstate.credit?.amount ?? 0
                                }
                            ]} />
                        </Grid>
                        <Grid xs={8}>
                            {/* Add credit rate chart here */}
                        </Grid>
                    </Grid>
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default ObjectCreditChartCard;