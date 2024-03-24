import {GetObjectQuery} from "@/generated/graphql";
import {Card, CardContent, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";
import {formatNumber} from "@/utilts/formatter";
import {useTranslation} from "next-export-i18n";

interface ObjectCreditDataCardProps {
    /**
     * Loading prop
     */
    loading: boolean;
    /**
     * The data
     */
    data: GetObjectQuery|undefined;
}

/**
 * Displays object credit status
 *
 * @constructor
 */
const ObjectCreditDataCard = ({loading, data}: ObjectCreditDataCardProps) => {

    const {t} = useTranslation();

    return (
        <Card variant="outlined">
            <CardContent>
                <LoadingSpinner loading={loading}>
                    <Typography level="h3">{t('common.creditRates')}</Typography>
                    <Typography>{t('credit.creditAmount')}: {formatNumber(data?.object?.realEstate.credit?.amount)}€</Typography>
                    <Typography>{t('common.bank')}: {data?.object?.realEstate.credit?.bank}</Typography>
                    <Typography>{t('common.interestRate')}: {formatNumber(data?.object?.realEstate.credit?.interestRate ?? 0)}%</Typography>
                    <Typography>{t('common.redemptionRate')}: {formatNumber(data?.object?.realEstate.credit?.redemptionRate ?? 0)}%</Typography>
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default ObjectCreditDataCard;