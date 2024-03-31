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
import {useTranslation} from "next-export-i18n";
import useCurrencySymbol from "@/hooks/useCurrencySymbol";


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

    const {t} = useTranslation();
    const currency = useCurrencySymbol();

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
                    <Typography level="h3">{t('object.baseData')}</Typography>
                    <Typography>{t('object.buyDate')}: {dayjs(data?.object?.realEstate.dateBought).format("DD.MM.YYYY")}</Typography>
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        value={data?.object?.realEstate.initialValue}
                        customDisplay={(v) => `${t('object.buyPrice')}: ${formatNumber(v as number)}${currency}`}
                        onChange={updateContent('initialValue')}
                        loading={mutationLoading}
                    />
                    <Typography>{t('object.estimatedCurrentMarketValue')}: {data?.object.estimatedMarketValue}{currency}</Typography>
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
                        customDisplay={(v) => `${t('object.rooms')}: ${v}`}
                        value={data?.object?.realEstate.rooms}
                        onChange={updateContent('rooms')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        customDisplay={(v) => `${t('object.usageSpace')}: ${v}m²`}
                        value={data?.object?.realEstate.space}
                        onChange={updateContent('space')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        customDisplay={(v) => `${t('object.objectType')}: ${v}`}
                        value={data?.object?.realEstate.objectType}
                        onChange={updateContent('objectType')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        customDisplay={(v) => `${t('object.constructionYear')}: ${v}`}
                        value={data?.object?.realEstate.constructionYear}
                        onChange={updateContent('constructionYear')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        customDisplay={(v) => `${t('object.renovationYear')}: ${v}`}
                        value={data?.object?.realEstate.renovationYear}
                        onChange={updateContent('renovationYear')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        customDisplay={(v) => `${t('object.energyEfficiency')}: ${v}`}
                        value={data?.object?.realEstate.energyEfficiency}
                        onChange={updateContent('energyEfficiency')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        customDisplay={(v) => `${t('object.return')}: ${formatNumber(v as number)}%`}
                        value={data?.object?.realEstate.grossReturn}
                        onChange={updateContent('grossReturn')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.BOOLEAN}
                        customDisplay={(v) => `${t('object.garden')}: ${v ? 'Ja' : 'Nein'}`}
                        value={data?.object?.realEstate.garden}
                        onChange={updateContent('garden')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.BOOLEAN}
                        customDisplay={(v) => `${t('object.kitchen')}: ${v ? 'Ja' : 'Nein'}`}
                        value={data?.object?.realEstate.kitchen}
                        onChange={updateContent('kitchen')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        customDisplay={(v) => `${t('object.heatingType')}: ${v}`}
                        value={data?.object?.realEstate.heatingType}
                        onChange={updateContent('heatingType')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        customDisplay={(v) => `${t('object.notes')}: ${v ? v : 'Keine'}`}
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