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
import {formatNumber} from "@/utilts/formatter";


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
                        customDisplay={(v) => `Kaufpreis: ${formatNumber(v as number)}€`}
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
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        customDisplay={(v) => `Räume: ${v}`}
                        value={data?.object?.realEstate.rooms}
                        onChange={updateContent('rooms')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        customDisplay={(v) => `Wohnfläche: ${v}m²`}
                        value={data?.object?.realEstate.space}
                        onChange={updateContent('space')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        customDisplay={(v) => `Objekt-typ: ${v}`}
                        value={data?.object?.realEstate.objectType}
                        onChange={updateContent('objectType')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        customDisplay={(v) => `Baujahr: ${v}`}
                        value={data?.object?.realEstate.constructionYear}
                        onChange={updateContent('constructionYear')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        customDisplay={(v) => `Renoviert: ${v}`}
                        value={data?.object?.realEstate.renovationYear}
                        onChange={updateContent('renovationYear')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        customDisplay={(v) => `Energie-effizienz: ${v}`}
                        value={data?.object?.realEstate.energyEfficiency}
                        onChange={updateContent('energyEfficiency')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        customDisplay={(v) => `Rendite: ${formatNumber(v as number)}%`}
                        value={data?.object?.realEstate.grossReturn}
                        onChange={updateContent('grossReturn')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.BOOLEAN}
                        customDisplay={(v) => `Garten: ${v ? 'Ja' : 'Nein'}`}
                        value={data?.object?.realEstate.garden}
                        onChange={updateContent('garden')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.BOOLEAN}
                        customDisplay={(v) => `Küche: ${v ? 'Ja' : 'Nein'}`}
                        value={data?.object?.realEstate.kitchen}
                        onChange={updateContent('kitchen')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        customDisplay={(v) => `Heizart: ${v}`}
                        value={data?.object?.realEstate.heatingType}
                        onChange={updateContent('heatingType')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        customDisplay={(v) => `Notizen: ${v ? v : 'Keine'}`}
                        value={data?.object?.realEstate.notes}
                        onChange={updateContent('notes')}
                        loading={mutationLoading}
                    />
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default ObjectBaseDataCard;