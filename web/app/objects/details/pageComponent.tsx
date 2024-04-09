"use client"
import {useRouter, useSearchParams} from "next/navigation";
import {Button, Divider, Grid, Option, Select, Stack, Typography} from "@mui/joy";
import {
    CreditDataFragment,
    CreditRateDataFragment, GetAllObjectsDocument, RentExpenseDataFragment, UploadedFileFragment,
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
import {useTranslation} from "next-export-i18n";
import ObjectRentSpreadTab from "@/components/object/ObjectRentSpreadTab";
import DocumentsTab from "@/components/object/DocumentsTab";


const ObjectDetailsPage = () => {

    const id = useSearchParams().get('id') ?? '';
    const router = useRouter();
    const {t} = useTranslation();
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
            label: t('common.general'),
            content: <ObjectDashboardTab loading={loading} data={data} />
        },
        {
            id: 'creditRates',
            label: t('common.creditRates'),
            content: <CreditRateList elements={(data?.object.realEstate.credit?.rates as CreditRateDataFragment[]) ?? []} />
        },
        {
            id: 'rentExpenses',
            label: t('object.expenses'),
            content: <ObjectRentSpreadTab expenses={(data?.object.realEstate.expenses as RentExpenseDataFragment[]) ?? []} />
        },
        {
            id: 'documents',
            label: t('common.documents'),
            content: <DocumentsTab
                docs={(data?.object.realEstate.uploadedFiles ?? []) as UploadedFileFragment[]}
                objectId={data?.object.realEstate.id}
                refetch={() => refetch({id: parseInt(id, 10)})}
            />
        },
        {
            id: 'priceChanges',
            label: t('credit.currentMarketPrice'),
            content: <ObjectPriceChangesTab loading={loading} data={data} fieldToAccess="priceChanges" />
        },
        {
            id: 'priceForecast',
            label: t('credit.priceForecast'),
            content: (
                <Stack spacing={2}>
                    <Select
                        variant="soft"
                        value={forecastYears}
                        onChange={(_, v ) => setForecastYears(v ?? 0)}
                        sx={{width: '200px'}}
                    >
                        {[5, 10, 15, 20, 25, 30, 35, 40, 45, 50].map((n) => (
                            <Option value={n} key={n}>{n} {t('common.years')}</Option>
                        ))}
                    </Select>
                    <ObjectPriceChangesTab loading={loading} data={data} fieldToAccess="priceForecast" />
                </Stack>
            )
        }
    ], [data, loading, forecastYears, t]);

    return (
        <>
            <Typography level="h1">
                {t('common.object')} {id}
            </Typography>
            <Grid container direction="row" spacing={2}>
                <Grid>
                    <Button
                        variant="solid"
                        color="primary"
                        sx={{width: '200px'}}
                        onClick={() => setCreditRateModalOpen(true)}
                    >
                        {t('credit.button.add-rate')}
                    </Button>
                </Grid>
                <Grid>
                    <Button
                        variant="solid"
                        color="primary"
                        sx={{width: '250px'}}
                        onClick={() => setConfigureAutoBookingModal(true)}
                    >
                        {t('credit.button.auto-booking')}
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
                        {t('common.delete')}
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