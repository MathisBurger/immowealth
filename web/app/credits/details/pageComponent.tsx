'use client';
import {CreditDataFragment, CreditRateDataFragment, useGetCreditQuery} from "@/generated/graphql";
import {useMemo, useState} from "react";
import {Button, Divider, Grid, Typography} from "@mui/joy";
import TabLayout, {TabLayoutElement} from "@/components/TabLayout";
import AddCreditRateModal from "@/components/object/modal/AddCreditRateModal";
import CreditRateList from "@/components/credit/CreditRateList";
import CreditDataTab from "@/components/credit/CreditDataTab";
import ConfigureCreditAutoPayModal from "@/components/credit/ConfigureCreditAutoPayModal";
import {useParams, useRouter, useSearchParams} from "next/navigation";
import MapsHomeWorkIcon from '@mui/icons-material/MapsHomeWork';


const CreditDetailsPage = () => {
    const id = useSearchParams().get('id') ?? '';

    const {data, loading} = useGetCreditQuery({
        variables: {id: parseInt(id, 10)}
    });
    const router = useRouter();

    const [creditRateModalOpen, setCreditRateModalOpen] = useState<boolean>(false);
    const [configureAutoBookingModal, setConfigureAutoBookingModal] = useState<boolean>(false);

    const tabs = useMemo<TabLayoutElement[]>(() => [
        {
            id: 'general',
            label: 'Allgemein',
            content: <CreditDataTab loading={loading} data={data} />
        },
        {
            id: 'creditRates',
            label: 'Kreditraten',
            content: <CreditRateList elements={(data?.credit.credit.rates as CreditRateDataFragment[]) ?? []} />
        }
    ], [data, loading]);

    return (
        <>
            <Typography level="h1">
                Kredit {id}
            </Typography>
            <Grid container direction="row" spacing={2}>
                <Grid>
                    <Button
                        variant="solid"
                        color="primary"
                        sx={{width: '200px'}}
                        onClick={() => setCreditRateModalOpen(true)}
                    >
                        Kreditrate hinzufügen
                    </Button>
                </Grid>
                <Grid>
                    <Button
                        variant="solid"
                        color="primary"
                        sx={{width: '250px'}}
                        onClick={() => setConfigureAutoBookingModal(true)}
                    >
                        Automatische Kreditbuchung
                    </Button>
                </Grid>
                <Grid>
                    <Button onClick={() => router.push('/objects/details?id=' + data?.credit.realEstateObjectId)}>
                        <MapsHomeWorkIcon />
                    </Button>
                </Grid>
            </Grid>
            <Divider />
            <TabLayout elements={tabs} />
            {creditRateModalOpen && (
                <AddCreditRateModal
                    onClose={() => setCreditRateModalOpen(false)}
                    creditId={data?.credit.credit.id}
                    objectId={undefined}
                />
            )}
            {configureAutoBookingModal && (
                <ConfigureCreditAutoPayModal
                    credit={data?.credit.credit as CreditDataFragment}
                    onClose={() => setConfigureAutoBookingModal(false)}
                    refetchId={data?.credit.credit.id ?? -1}
                />
            )}
        </>
    );
}

export const dynamic = 'force-static';
export const dynamicParams = true;

export default CreditDetailsPage;