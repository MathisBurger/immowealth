import {Grid} from "@mui/joy";
import ObjectCreditChartCard from "@/components/object/ObjectCreditChartCard";
import {GetCreditQuery} from "@/generated/graphql";
import CreditDataCard from "@/components/credit/CreditDataCard";
import CreditChartCard from "@/components/credit/CreditChartCard";

interface CreditDataTabProps {
    loading: boolean;
    data: GetCreditQuery|undefined;
}

const CreditDataTab = ({loading, data}: CreditDataTabProps) => {

    return (
        <Grid container direction="row" spacing={2}>
            <Grid xs={4} container direction="column">
                <CreditDataCard loading={loading} data={data} />
            </Grid>
            <Grid xs={8}>
                <CreditChartCard loading={loading} data={data} />
            </Grid>
        </Grid>
    );
}

export default CreditDataTab;