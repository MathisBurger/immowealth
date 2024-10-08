'use client';
import {
    CreditDataFragment,
    CreditRateDataFragment,
    useGetCreditQuery,
    useMarkAsFavouriteMutation, useUnmarkAsFavouriteMutation
} from "@/generated/graphql";
import {useMemo, useState} from "react";
import {Button, Divider, Grid, Typography} from "@mui/joy";
import TabLayout, {TabLayoutElement} from "@/components/TabLayout";
import AddCreditRateModal from "@/components/object/modal/AddCreditRateModal";
import CreditRateList from "@/components/credit/CreditRateList";
import CreditDataTab from "@/components/credit/CreditDataTab";
import ConfigureCreditAutoPayModal from "@/components/credit/ConfigureCreditAutoPayModal";
import {useParams, useRouter, useSearchParams} from "next/navigation";
import MapsHomeWorkIcon from '@mui/icons-material/MapsHomeWork';
import {useTranslation} from "next-export-i18n";
import useCurrentUser from "@/hooks/useCurrentUser";


const CreditDetailsPage = () => {
    const id = useSearchParams().get('id') ?? '';
    const {t} = useTranslation();

    const {data, loading, refetch} = useGetCreditQuery({
        variables: {id: parseInt(id, 10)}
    });
    const [markAsFavouriteMutation] = useMarkAsFavouriteMutation();
    const [unmarkAsFavouriteMutation] = useUnmarkAsFavouriteMutation();
    const currentUser = useCurrentUser();

    const isFavourite = useMemo<boolean>(() => {
        // @ts-ignore
        return data?.credit.credit.favourite?.filter((f) => f?.id === currentUser?.id)?.length > 0;
    }, [data, currentUser]);
    const router = useRouter();

    const [creditRateModalOpen, setCreditRateModalOpen] = useState<boolean>(false);
    const [configureAutoBookingModal, setConfigureAutoBookingModal] = useState<boolean>(false);

    const tabs = useMemo<TabLayoutElement[]>(() => [
        {
            id: 'general',
            label: t('credit.tab.general'),
            content: <CreditDataTab loading={loading} data={data} />
        },
        {
            id: 'creditRates',
            label: t('credit.tab.rates'),
            content: <CreditRateList elements={(data?.credit.credit.rates as CreditRateDataFragment[]) ?? []} />
        }
    ], [data, loading, t]);

    return (
        <>
            <Typography level="h1">
                {t('common.credit')} {id}
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
                    {isFavourite ? (
                        <Button
                            variant="solid"
                            color="primary"
                            sx={{width: '250px'}}
                            onClick={() => {
                                unmarkAsFavouriteMutation({
                                    variables: {entityName: 'de.immowealth.entity.Credit', id: parseInt(`${id}`, 10)}
                                }).then(() => refetch())
                            }}
                        >
                            {t('common.unmarkAsFavourite')}
                        </Button>
                    ) : (
                        <Button
                            variant="solid"
                            color="primary"
                            sx={{width: '250px'}}
                            onClick={() => {
                                markAsFavouriteMutation({
                                    variables: {entityName: 'de.immowealth.entity.Credit', id: parseInt(`${id}`, 10)}
                                }).then(() => refetch())
                            }}
                        >
                            {t('common.markAsFavourite')}
                        </Button>
                    )}
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