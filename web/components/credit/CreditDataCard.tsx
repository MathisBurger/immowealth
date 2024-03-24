import {Card, CardContent, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";
import {
    GetCreditDocument,
    GetCreditQuery,
    GetObjectDocument, UpdateCreditInputInput,
    useUpdateCreditMutation
} from "@/generated/graphql";
import EditableDisplay, {DisplayValue, InputType} from "@/components/EditableDisplay";
import {formatNumber} from "@/utilts/formatter";
import {useTranslation} from "next-export-i18n";

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

    const [mutation, {loading: mutationLoading}] = useUpdateCreditMutation({
        refetchQueries: [
            {
                query: GetCreditDocument,
                variables: {id: data?.credit.credit.id ?? -1}
            }
        ]
    });

    const {t} = useTranslation();

    const updateContent = (field: keyof UpdateCreditInputInput) => async (val: DisplayValue): Promise<boolean> => {
        const result = await mutation({
            variables: {input: {
                    [field]: val,
                    id: data?.credit.credit.id ?? -1
                }}
        });
        return result.errors === undefined;
    }

    return (
        <Card variant="outlined">
            <CardContent>
                <LoadingSpinner loading={loading}>
                    <Typography level="h3">{t('common.creditRates')}</Typography>
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        value={data?.credit.credit.amount}
                        customDisplay={(v) => `${t('credit.creditAmount')}: ${formatNumber(v as number)}€`}
                        onChange={updateContent('amount')}
                        loading={mutationLoading}
                    />
                    <Typography>Getilgt: {data?.credit.creditRateSum}€</Typography>
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        value={data?.credit.credit.bank}
                        customDisplay={(v) => `${t('common.bank')}: ${v}`}
                        onChange={updateContent('bank')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        value={data?.credit.credit.interestRate}
                        customDisplay={(v) => `${t('common.interestRate')}: ${formatNumber(v as number)}%`}
                        onChange={updateContent('interestRate')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        value={data?.credit.credit.redemptionRate}
                        customDisplay={(v) => `${t('common.redemptionRate')}: ${formatNumber(v as number)}%`}
                        onChange={updateContent('redemptionRate')}
                        loading={mutationLoading}
                    />
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default CreditDataCard;