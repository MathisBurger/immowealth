import {GetObjectQuery} from "@/generated/graphql";
import {Card, CardContent, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";
import dayjs from "dayjs";
import EditableDisplay, {InputType} from "@/components/EditableDisplay";


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
                    <Typography>Kaufdatum: {dayjs(data?.object?.realEstate.dateBought).format("DD.MM.YYYY")}</Typography>
                    <Typography>Kaufpreis: {data?.object?.realEstate.initialValue}€</Typography>
                    <Typography>Geschätzter Marktpreis (aktuell): {data?.object.estimatedMarketValue}€</Typography>
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        value={data?.object?.realEstate.streetAndHouseNr}
                        onChange={() => true}
                        loading={false}
                    />
                    <Typography>{data?.object?.realEstate.streetAndHouseNr}</Typography>
                    <Typography>{data?.object?.realEstate.zip} {data?.object?.realEstate.city}</Typography>
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default ObjectBaseDataCard;