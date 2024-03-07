import {Card, CardContent, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";
import {GetCreditQuery} from "@/generated/graphql";

interface CreditDataCardProps {
    /**
     * Loading prop
     */
    loading: boolean;
    /**
     * The data
     */
    data: GetCreditQuery|undefined;
}

/**
 * Displays the credit data
 *
 * @constructor
 */
const CreditDataCard = ({loading, data}: CreditDataCardProps) => {

    return (
        <Card variant="outlined">
            <CardContent>
                <LoadingSpinner loading={loading}>
                    <Typography level="h3">Kreditdaten</Typography>
                    <Typography>Kredithöhe: {data?.credit.credit.amount}€</Typography>
                    <Typography>Getilgt: {data?.credit.creditRateSum}€</Typography>
                    <Typography>Bank: {data?.credit.credit.bank}</Typography>
                    <Typography>Zins: {data?.credit.credit.interestRate}%</Typography>
                    <Typography>Tilgung: {data?.credit.credit.redemptionRate}%</Typography>
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default CreditDataCard;