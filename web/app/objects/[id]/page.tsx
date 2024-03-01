'use client';
import {useParams} from "next/navigation";
import {Card, CardContent, Divider, Grid, Typography} from "@mui/joy";
import {useGetObjectQuery} from "@/generated/graphql";
import LoadingSpinner from "@/components/LoadingSpinner";
import {BarChart} from "@mui/x-charts";
import ObjectBaseDataCard from "@/components/object/ObjectBaseDataCard";
import ObjectCreditChartCard from "@/components/object/ObjectCreditChartCard";


const ObjectDetailsPage = () => {

    const {id} = useParams<{id: string}>();
    const {data, loading} = useGetObjectQuery({
        variables: {id: parseInt(id, 10)}
    });
    return (
        <>
            <Typography level="h1">
                Objekt {id}
            </Typography>
            <Divider />
            <Grid container direction="row">
                <Grid xs={4}>
                    <ObjectBaseDataCard loading={loading} data={data} />
                </Grid>
                <Grid xs={8}>
                    <ObjectCreditChartCard loading={loading} data={data} />
                </Grid>
            </Grid>
        </>
    );
}

export default ObjectDetailsPage;