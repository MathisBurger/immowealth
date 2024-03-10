import {Card, CardContent, Typography} from "@mui/joy";
import LoadingSpinner from "@/components/LoadingSpinner";
import {
    GetCreditDocument,
    GetCreditQuery,
    GetObjectDocument, UpdateCreditInputInput,
    useUpdateCreditMutation
} from "@/generated/graphql";
import EditableDisplay, {DisplayValue, InputType} from "@/components/EditableDisplay";

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
                    <Typography level="h3">Kreditdaten</Typography>
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        value={data?.credit.credit.amount}
                        customDisplay={(v) => `Kredithöhe: ${v}€`}
                        onChange={updateContent('amount')}
                        loading={mutationLoading}
                    />
                    <Typography>Getilgt: {data?.credit.creditRateSum}€</Typography>
                    <EditableDisplay
                        inputType={InputType.TEXT}
                        value={data?.credit.credit.bank}
                        customDisplay={(v) => `Bank: ${v}`}
                        onChange={updateContent('bank')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        value={data?.credit.credit.interestRate}
                        customDisplay={(v) => `Zins: ${v}%`}
                        onChange={updateContent('interestRate')}
                        loading={mutationLoading}
                    />
                    <EditableDisplay
                        inputType={InputType.NUMBER}
                        value={data?.credit.credit.redemptionRate}
                        customDisplay={(v) => `Tilgung: ${v}%`}
                        onChange={updateContent('redemptionRate')}
                        loading={mutationLoading}
                    />
                </LoadingSpinner>
            </CardContent>
        </Card>
    );
}

export default CreditDataCard;