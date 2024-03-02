'use client';
import {useParams} from "next/navigation";
import {Button, Divider, Grid, Typography} from "@mui/joy";
import {CreditRateDataFragment, useGetObjectQuery} from "@/generated/graphql";
import {useMemo, useState} from "react";
import AddCreditRateModal from "@/components/object/modal/AddCreditRateModal";
import TabLayout, {TabLayoutElement} from "@/components/TabLayout";
import ObjectDashboardTab from "@/components/object/ObjectDashboardTab";
import CreditRateList from "@/components/credit/CreditRateList";


const ObjectDetailsPage = () => {

    const {id} = useParams<{id: string}>();
    const {data, loading} = useGetObjectQuery({
        variables: {id: parseInt(id, 10)}
    });

    const [creditRateModalOpen, setCreditRateModalOpen] = useState<boolean>(false);

    const tabs = useMemo<TabLayoutElement[]>(() => [
        {
            id: 'general',
            label: 'Allgemein',
            content: <ObjectDashboardTab loading={loading} data={data} />
        },
        {
            id: 'creditRates',
            label: 'Kreditraten',
            content: <CreditRateList elements={(data?.object.realEstate.credit?.rates as CreditRateDataFragment[]) ?? []} />
        }
    ], [data, loading]);

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
            <TabLayout elements={tabs} />
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