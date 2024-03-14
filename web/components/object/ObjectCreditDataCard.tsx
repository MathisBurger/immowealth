import {GetObjectQuery} from "@/generated/graphql";
import {Card, CardContent, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";
import {formatNumber} from "@/utilts/formatter";

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

    return (
        <Card variant="outlined">
            <CardContent>
                <LoadingSpinner loading={loading}>
                    <Typography level="h3">Kreditdaten</Typography>
                    <Typography>Kredithöhe: {formatNumber(data?.object?.realEstate.credit?.amount)}€</Typography>
                    <Typography>Bank: {data?.object?.realEstate.credit?.bank}</Typography>
                    <Typography>Zins: {formatNumber(data?.object?.realEstate.credit?.interestRate ?? 0)}%</Typography>
                    <Typography>Tilgung: {formatNumber(data?.object?.realEstate.credit?.redemptionRate ?? 0)}%</Typography>
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default ObjectCreditDataCard;