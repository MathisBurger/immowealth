import {GetObjectQuery} from "@/generated/graphql";
import {Card, CardContent, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";


interface ObjectBaseDataCard {
    loading: boolean;
    data: GetObjectQuery|undefined;
}

const ObjectBaseDataCard = ({loading, data}: ObjectBaseDataCard) => {

    return (
        <Card variant="outlined">
            <CardContent>
                <LoadingSpinner loading={loading}>
                    <Typography level="h3">Grunddaten</Typography>
                    <Typography>Kaufdatum: {data?.object?.realEstate.dateBought}</Typography>
                    <Typography>{data?.object?.realEstate.streetAndHouseNr}</Typography>
                    <Typography>{data?.object?.realEstate.zip} {data?.object?.realEstate.city}</Typography>
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default ObjectBaseDataCard;