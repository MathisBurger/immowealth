import {
    GetObjectDocument,
    GetObjectQuery,
    UpdateRealEstateInputInput,
    useUpdateRealEstateMutation
} from "@/generated/graphql";
import {Card, CardContent, Grid, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";
import dayjs from "dayjs";
import EditableDisplay, {DisplayValue, InputType} from "@/components/EditableDisplay";


interface ObjectBaseDataCard {
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
 * Real estate base data
 *
 * @constructor
 */
const ObjectBaseDataCard = ({loading, data}: ObjectBaseDataCard) => {

    const [mutation, {loading: mutationLoading}] = useUpdateRealEstateMutation({
        refetchQueries: [
            {
                query: GetObjectDocument,
                variables: {id: data?.object.realEstate.id ?? -1}
            }
        ]
    });

    const updateContent = (field: keyof UpdateRealEstateInputInput) => async (val: DisplayValue): Promise<boolean> => {
        const result = await mutation({
            variables: {input: {
                    [field]: val,
                    id: data?.object.realEstate.id ?? -1
                }}
        });
        return result.errors === undefined;
    }

    return (
        <Card variant="outlined">
            <CardContent>
                <LoadingSpinner loading={loading}>
                    <Typography level="h3">Grunddaten</Typography>
                    <Typography>Kaufdatum: {dayjs(data?.object?.realEstate.dateBought).format("DD.MM.YYYY")}</Typography>
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        value={data?.object?.realEstate.initialValue}
                        customDisplay={(v) => `Kaufpreis: ${v}€`}
                        onChange={updateContent('initialValue')}
                        loading={mutationLoading}
                    />
                    <Typography>Geschätzter Marktpreis (aktuell): {data?.object.estimatedMarketValue}€</Typography>
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        value={data?.object?.realEstate.streetAndHouseNr}
                        onChange={updateContent('streetAndHouseNr')}
                        loading={mutationLoading}
                    />
                    <Grid container direction="row" spacing={2}>
                        <Grid>
                            <EditableDisplay
                                inputType={InputType.TEXT}
                                value={data?.object?.realEstate.zip}
                                onChange={updateContent('zip')}
                                loading={mutationLoading}
                            />
                        </Grid>
                        <Grid>
                            <EditableDisplay
                                inputType={InputType.TEXT}
                                value={data?.object?.realEstate.city}
                                onChange={updateContent('city')}
                                loading={mutationLoading}
                            />
                        </Grid>
                    </Grid>
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default ObjectBaseDataCard;