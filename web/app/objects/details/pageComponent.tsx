"use client"
import {useRouter, useSearchParams} from "next/navigation";
import {Button, Divider, Grid, Option, Select, Stack, Typography} from "@mui/joy";
import {
    CreditDataFragment,
    CreditRateDataFragment, GetAllObjectsDocument,
    useDeleteRealEstateMutation,
    useGetObjectQuery
} from "@/generated/graphql";
import {useEffect, useMemo, useState} from "react";
import AddCreditRateModal from "@/components/object/modal/AddCreditRateModal";
import TabLayout, {TabLayoutElement} from "@/components/TabLayout";
import ObjectDashboardTab from "@/components/object/ObjectDashboardTab";
import CreditRateList from "@/components/credit/CreditRateList";
import ObjectPriceChangesTab from "@/components/object/ObjectPriceChangesTab";
import ConfigureCreditAutoPayModal from "@/components/credit/ConfigureCreditAutoPayModal";


const ObjectDetailsPage = () => {

    const id = useSearchParams().get('id') ?? '';
    const router = useRouter();
    const {data, loading, refetch} = useGetObjectQuery({
        variables: {id: parseInt(id, 10)}
    });

    const [deleteMutation, {loading: deleteLoading}] = useDeleteRealEstateMutation({
        refetchQueries: [
            {
                query: GetAllObjectsDocument
            }
        ]
    });

    const deleteObject = async () => {
        const result = await deleteMutation({
            variables: {id: parseInt(`${id}`)}
        });
        if (result.errors === undefined) {
            router.push('/objects');
        }
    }

    const [creditRateModalOpen, setCreditRateModalOpen] = useState<boolean>(false);
    const [configureAutoBookingModal, setConfigureAutoBookingModal] = useState<boolean>(false);
    const [forecastYears, setForecastYears] = useState<number>(10);

    useEffect(() => {
        refetch({id: parseInt(id, 10), yearsInFuture: forecastYears});
    }, [forecastYears, id, refetch])

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
                            <Option value={n} key={n}>{n} Jahre</Option>
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
                    <Button
                        variant="solid"
                        color="danger"
                        sx={{width: '250px'}}
                        loading={deleteLoading}
                        onClick={deleteObject}
                    >
                        Löschen
                    </Button>
                </Grid>
            </Grid>
            <Divider />
            <TabLayout elements={tabs} />
            {creditRateModalOpen && (
                <AddCreditRateModal
                    onClose={() => setCreditRateModalOpen(false)}
                    creditId={data?.object.realEstate.credit?.id ?? -1}
                    objectId={data?.object.realEstate.id ?? -1}
                />
            )}
            {configureAutoBookingModal && (
                <ConfigureCreditAutoPayModal
                    credit={data?.object.realEstate.credit as CreditDataFragment}
                    onClose={() => setConfigureAutoBookingModal(false)}
                    refetchId={data?.object.realEstate.id ?? -1}
                    isObjectRefetch
                />
            )}
        </>
    );
}

export const dynamic = 'force-static';
export const dynamicParams = true;

export default ObjectDetailsPage;