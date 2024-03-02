'use client';
import {useParams} from "next/navigation";
import {CreditRateDataFragment, useGetCreditQuery} from "@/generated/graphql";
import {useMemo, useState} from "react";
import {Button, Divider, Typography} from "@mui/joy";
import TabLayout, {TabLayoutElement} from "@/components/TabLayout";
import AddCreditRateModal from "@/components/object/modal/AddCreditRateModal";
import CreditRateList from "@/components/credit/CreditRateList";
import CreditDataTab from "@/components/credit/CreditDataTab";


const CreditDetailsPage = () => {
    const {id} = useParams<{id: string}>();

    const {data, loading} = useGetCreditQuery({
        variables: {id: parseInt(id, 10)}
    });

    const [creditRateModalOpen, setCreditRateModalOpen] = useState<boolean>(false);

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
    ], [data]);

    return (
        <>
            <Typography level="h1">
                Kredit {id}
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
                    creditId={data?.credit.credit.id}
                    objectId={undefined}
                />
            )}
        </>
    );
}

export default CreditDetailsPage;