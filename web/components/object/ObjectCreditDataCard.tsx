import {GetObjectQuery} from "@/generated/graphql";
import {Card, CardContent, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";

interface ObjectCreditDataCardProps {
    loading: boolean;
    data: GetObjectQuery|undefined;
}

const ObjectCreditDataCard = ({loading, data}: ObjectCreditDataCardProps) => {

    return (
        <Card variant="outlined">
            <CardContent>
                <LoadingSpinner loading={loading}>
                    <Typography level="h3">Kreditdaten</Typography>
                    <Typography>Kredithöhe: {data?.object?.realEstate.credit?.amount}€</Typography>
                    <Typography>Bank: {data?.object?.realEstate.credit?.bank}</Typography>
                    <Typography>Zins: {data?.object?.realEstate.credit?.interestRate}%</Typography>
                    <Typography>Tilgung: {data?.object?.realEstate.credit?.redemptionRate}%</Typography>
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default ObjectCreditDataCard;