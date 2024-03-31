import {useMemo} from "react";
import {Card, CardContent, Grid, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";
import {BarChart, BarSeriesType, LineChart} from "@mui/x-charts";
import {GetCreditQuery} from "@/generated/graphql";
import {useTranslation} from "next-export-i18n";

interface CreditChartCardProps {
    /**
     * If data is loading
     */
    loading: boolean;
    /**
     * The actual data
     */
    data: GetCreditQuery|undefined;
}

/**
 * Bar chart data type
 */
type BarChartData = Omit<BarSeriesType, 'type'>;


/**
 * Displays credit in a chart
 *
 * @constructor
 */
const CreditChartCard = ({loading, data}: CreditChartCardProps) => {

    const series = useMemo<BarChartData[]>(() => [
        {dataKey: 'paid', label: 'Getilgt'},
        {dataKey: 'total', label: 'Kredit'},
    ], []);

    const {t} = useTranslation();

    return (
        <Card variant="outlined">
            <CardContent>
                <LoadingSpinner loading={loading}>
                    <Typography level="h3">{t('credit.financingHistory')}</Typography>
                    <Grid container direction="row">
                        <Grid xs={4}>
                            <BarChart series={series} height={400} dataset={[
                                {
                                    paid: data?.credit.creditRateSum ?? 0,
                                    total: data?.credit.credit.amount ?? 0
                                }
                            ]} />
                        </Grid>
                        <Grid xs={8}>
                            <LineChart
                                series={[{data: data?.credit.creditRateCummulationSteps.filter(e => e !== null)}]}
                            />
                        </Grid>
                    </Grid>
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default CreditChartCard;