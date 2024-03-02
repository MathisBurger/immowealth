'use client';
import {useParams} from "next/navigation";
import {Button, Divider, Option, Select, Stack, Typography} from "@mui/joy";
import {CreditRateDataFragment, useGetObjectQuery} from "@/generated/graphql";
import {useEffect, useMemo, useState} from "react";
import AddCreditRateModal from "@/components/object/modal/AddCreditRateModal";
import TabLayout, {TabLayoutElement} from "@/components/TabLayout";
import ObjectDashboardTab from "@/components/object/ObjectDashboardTab";
import CreditRateList from "@/components/credit/CreditRateList";
import ObjectPriceChangesTab from "@/components/object/ObjectPriceChangesTab";


const ObjectDetailsPage = () => {

    const {id} = useParams<{id: string}>();
    const {data, loading, refetch} = useGetObjectQuery({
        variables: {id: parseInt(id, 10)}
    });

    const [creditRateModalOpen, setCreditRateModalOpen] = useState<boolean>(false);
    const [forecastYears, setForecastYears] = useState<number>(10);

    useEffect(() => {
        console.log("is this the dream");
        refetch({id: parseInt(id, 10), yearsInFuture: forecastYears});
    }, [forecastYears])

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
        },
        {
            id: 'priceChanges',
            label: 'Marktwert (aktuell)',
            content: <ObjectPriceChangesTab loading={loading} data={data} fieldToAccess="priceChanges" />
        },
        {
            id: 'priceForecast',
            label: 'Marktwert (Prognose)',
            content: (
                <Stack spacing={2}>
                    <Select
                        variant="soft"
                        value={forecastYears}
                        onChange={(_, v ) => setForecastYears(v ?? 0)}
                        sx={{width: '200px'}}
                    >
                        {[5, 10, 15, 20, 25, 30, 35, 40, 45, 50].map((n) => (
                            <Option value={n}>{n} Jahre</Option>
                        ))}
                    </Select>
                    <ObjectPriceChangesTab loading={loading} data={data} fieldToAccess="priceForecast" />
                </Stack>
            )
        }
    ], [data, loading, forecastYears]);

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