import {Card, CardContent, Grid, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";
import {BarChart, BarSeriesType, LineChart} from "@mui/x-charts";
import {GetObjectQuery} from "@/generated/graphql";
import {useMemo} from "react";

interface ObjectCreditChartCardProps {
    /**
     * Loading prop
     */
    loading: boolean;
    /**
     * The data
     */
    data: GetObjectQuery|undefined;
}

/**
 * Type of bar chart
 */
type BarChartData = Omit<BarSeriesType, 'type'>;

/**
 * Credit chart card of objects
 *
 * @constructor
 */
const ObjectCreditChartCard = ({loading, data}: ObjectCreditChartCardProps) => {

    const series = useMemo<BarChartData[]>(() => [
        {dataKey: 'paid', label: 'Getilgt'},
        {dataKey: 'total', label: 'Kredit'},
    ], []);

    return (
        <Card variant="outlined">
            <CardContent>
                <LoadingSpinner loading={loading}>
                    <Typography level="h3">Finanzierungsverlauf</Typography>
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
                           <LineChart
                               series={[{data: data?.object.creditRateCummulationSteps.filter(e => e !== null)}]}
                           />
                        </Grid>
                    </Grid>
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default ObjectCreditChartCard;