'use client';
import {useParams} from "next/navigation";
import {Button, Divider, Grid, Typography} from "@mui/joy";
import {useGetObjectQuery} from "@/generated/graphql";
import ObjectBaseDataCard from "@/components/object/ObjectBaseDataCard";
import ObjectCreditChartCard from "@/components/object/ObjectCreditChartCard";
import {useState} from "react";
import AddCreditRateModal from "@/components/object/modal/AddCreditRateModal";
import ObjectCreditDataCard from "@/components/object/ObjectCreditDataCard";


const ObjectDetailsPage = () => {

    const {id} = useParams<{id: string}>();
    const {data, loading} = useGetObjectQuery({
        variables: {id: parseInt(id, 10)}
    });

    const [creditRateModalOpen, setCreditRateModalOpen] = useState<boolean>(false);

    return (
        <>
            <Typography level="h1">
                Objekt {id}
            </Typography>
            <Button
                variant="solid"
                color="primary"
                sx={{width: '200px'}}
                onClick={() => setCreditRateModalOpen(true)}
            >
                Kreditrate hinzufügen
            </Button>
            <Divider />
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
            {creditRateModalOpen && (
                <AddCreditRateModal
                    onClose={() => setCreditRateModalOpen(false)}
                    creditId={data?.object.realEstate.credit?.id ?? -1}
                    objectId={data?.object.realEstate.id ?? -1}
                />
            )}
        </>
    );
}

export default ObjectDetailsPage;