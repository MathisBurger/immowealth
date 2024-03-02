import {Grid} from "@mui/joy";
import ObjectBaseDataCard from "@/components/object/ObjectBaseDataCard";
import ObjectCreditDataCard from "@/components/object/ObjectCreditDataCard";
import ObjectCreditChartCard from "@/components/object/ObjectCreditChartCard";
import {GetObjectQuery} from "@/generated/graphql";

interface ObjectDashboardTab {
    loading: boolean;
    data: GetObjectQuery|undefined;
}

const ObjectDashboardTab = ({loading, data}: ObjectDashboardTab) => {

    return (
        <Grid container direction="row" spacing={2}>
            <Grid xs={4} container direction="column">
                <Grid xs={12}>
                    <ObjectBaseDataCard loading={loading} data={data} />
                </Grid>
                <Grid xs={12}>
                    <ObjectCreditDataCard loading={loading} data={data} />
                </Grid>
            </Grid>
            <Grid xs={8}>
                <ObjectCreditChartCard loading={loading} data={data} />
            </Grid>
        </Grid>
    );
}

export default ObjectDashboardTab;