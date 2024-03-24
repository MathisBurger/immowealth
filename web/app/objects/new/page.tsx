'use client';
import {
    Button,
    Card,
    CardContent, Checkbox,
    Divider,
    FormControl,
    FormLabel,
    Grid,
    Input,
    Stack,
    Textarea,
    Typography
} from "@mui/joy";
import {DatePicker} from "@mui/x-date-pickers";
import {useRouter} from "next/navigation";
import {FormEvent} from "react";
import {GetAllObjectsDocument, useCreateRealEstateMutation} from "@/generated/graphql";
import {useTranslation} from "next-export-i18n";


const NewObject = () => {

    const router = useRouter();
    const [mutation, {loading}] = useCreateRealEstateMutation({
        refetchQueries: [{query: GetAllObjectsDocument}]
    });
    const {t} = useTranslation();

    const submit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        let formData = new FormData(e.currentTarget);
        let result = await mutation({
            variables: {input: {
                    city: formData.get("city") as string,
                    credit: {
                        amount: parseInt(`${formData.get("creditAmount")}`, 10),
                        bank: formData.get("bank") as string,
                        interestRate: parseFloat(`${formData.get("interestRate")}`),
                        redemptionRate: parseFloat(`${formData.get("redemptionRate")}`),
                    },
                    dateBought: new Date(formData.get("dateBought") as string),
                    initialValue: parseInt(`${formData.get("initialPrice")}`, 10),
                    streetAndHouseNr: formData.get("streetAndHouseNr") as string,
                    zip: formData.get("zip") as string,
                    rooms: parseFloat(`${formData.get('rooms')}`),
                    space: parseFloat(`${formData.get('space')}`),
                    objectType: `${formData.get('objectType')}`,
                    constructionYear: parseInt(`${formData.get('space')}`, 10),
                    renovationYear: formData.get('renovationYear') ? parseInt(`${formData.get('space')}`, 10) : null,
                    energyEfficiency: formData.get('energyEfficiency') ? `${formData.get('energyEfficiency')}` : null,
                    grossReturn: parseFloat(`${formData.get('grossReturn')}`),
                    garden: formData.get('garden') === 'true',
                    kitchen: formData.get('kitchen') === 'true',
                    heatingType: `${formData.get('heatingType')}`,
                    notes: formData.get('notes') ? `${formData.get('objectType')}` : null,
                }}
        });
        if (result.errors === undefined) {
            router.push(`/objects/details?id=${result.data?.createRealEstate.id}`);
        }
    }

    return (
        <>
            <Typography level="h1">{t('object.new')}</Typography>
            <Divider />
            <form onSubmit={submit}>
                <Grid container direction="row" spacing={2}>
                    <Grid xs={6}>
                        <Card>
                            <CardContent>
                                <Stack spacing={2}>
                                    <Typography level="h3">{t('object.baseData')}</Typography>
                                    <FormControl>
                                        <FormLabel>{t('object.streetAndHouseNr')} *</FormLabel>
                                        <Input
                                            required
                                            name="streetAndHouseNr"
                                            placeholder={t('object.streetAndHouseNr')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <Grid container direction="row" spacing={2}>
                                        <Grid xs={6}>
                                            <FormControl>
                                                <FormLabel>{t('common.zip')} *</FormLabel>
                                                <Input
                                                    required
                                                    name="zip"
                                                    placeholder={t('common.zip')}
                                                    variant="soft"
                                                    fullWidth
                                                />
                                            </FormControl>
                                        </Grid>
                                        <Grid xs={6}>
                                            <FormControl>
                                                <FormLabel>{t('common.city')} *</FormLabel>
                                                <Input
                                                    required
                                                    name="city"
                                                    placeholder={t('common.city')}
                                                    variant="soft"
                                                    fullWidth
                                                />
                                            </FormControl>
                                        </Grid>
                                    </Grid>
                                    <FormControl>
                                        <FormLabel>{t('object.buyPrice')} *</FormLabel>
                                        <Input
                                            required
                                            name="initialPrice"
                                            placeholder={t('object.buyPrice')}
                                            variant="soft"
                                            type="number"
                                            fullWidth
                                            endDecorator={"€"}
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('object.buyDate')} *</FormLabel>
                                        <DatePicker disableFuture name="dateBought" />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('object.rooms')} *</FormLabel>
                                        <Input
                                            required
                                            name="rooms"
                                            type="number"
                                            placeholder={t('object.rooms')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('object.usageSpace')} *</FormLabel>
                                        <Input
                                            required
                                            name="space"
                                            placeholder={t('object.usageSpace')}
                                            variant="soft"
                                            type="number"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('object.objectType')} *</FormLabel>
                                        <Input
                                            required
                                            name="objectType"
                                            placeholder={t('object.objectType')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('object.constructionYear')} *</FormLabel>
                                        <Input
                                            required
                                            name="constructionYear"
                                            placeholder={t('object.constructionYear')}
                                            variant="soft"
                                            type="number"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('object.renovationYear')}</FormLabel>
                                        <Input
                                            name="renovationYear"
                                            type="number"
                                            placeholder={t('object.renovationYear')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('object.energyEfficiency')}</FormLabel>
                                        <Input
                                            name="energyEfficiency"
                                            placeholder={t('object.energyEfficiency')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('object.return')} *</FormLabel>
                                        <Input
                                            required
                                            type="number"
                                            name="grossReturn"
                                            placeholder={t('object.return')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('object.heatingType')} *</FormLabel>
                                        <Input
                                            required
                                            name="heatingType"
                                            placeholder={t('object.heatingType')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <Checkbox name="garden" label={t('object.garden')} />
                                    <Checkbox name="kitchen" label={t('object.kitchen')} />
                                    <FormControl>
                                        <FormLabel>{t('object.notes')}</FormLabel>
                                        <Textarea
                                            name="notes"
                                            placeholder={t('object.notes')}
                                            variant="soft"
                                        />
                                    </FormControl>
                                </Stack>
                            </CardContent>
                        </Card>
                    </Grid>
                    <Grid xs={6}>
                        <Card>
                            <CardContent>
                                <Stack spacing={2}>
                                    <Typography level="h3">{t('common.creditRates')}</Typography>
                                    <FormControl>
                                        <FormLabel>{t('common.bank')} *</FormLabel>
                                        <Input
                                            required
                                            name="bank"
                                            placeholder={t('common.bank')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('common.creditSum')} *</FormLabel>
                                        <Input
                                            required
                                            name="creditAmount"
                                            placeholder={t('common.creditSum')}
                                            variant="soft"
                                            type="number"
                                            fullWidth
                                            endDecorator={"€"}
                                        />
                                    </FormControl>
                                    <Grid container direction="row" spacing={2}>
                                        <Grid xs={6}>
                                            <FormControl>
                                                <FormLabel>{t('common.interestRate')} *</FormLabel>
                                                <Input
                                                    required
                                                    name="interestRate"
                                                    placeholder={t('common.interestRate')}
                                                    variant="soft"
                                                    type="number"
                                                    fullWidth
                                                    endDecorator={"%"}
                                                />
                                            </FormControl>
                                        </Grid>
                                        <Grid xs={6}>
                                            <FormControl>
                                                <FormLabel>{t('common.redemptionRate')} *</FormLabel>
                                                <Input
                                                    required
                                                    name="redemptionRate"
                                                    placeholder={t('common.redemptionRate')}
                                                    variant="soft"
                                                    type="number"
                                                    fullWidth
                                                    endDecorator={"%"}
                                                />
                                            </FormControl>
                                        </Grid>
                                    </Grid>
                                </Stack>
                            </CardContent>
                        </Card>
                    </Grid>
                    <Grid xs={12}>
                        <Card>
                            <CardContent>
                                <Grid container direction="row" spacing={2} alignItems="flex-end">
                                    <Grid xs={8}>
                                        <Typography level="h4">{t('common.actions')}</Typography>
                                    </Grid>
                                    <Grid xs={2}>
                                        <Button
                                            variant="solid"
                                            color="neutral"
                                            fullWidth
                                            onClick={() => router.back()}
                                        >
                                            {t('common.cancel')}
                                        </Button>
                                    </Grid>
                                    <Grid xs={2}>
                                        <Button
                                            variant="solid"
                                            color="primary"
                                            fullWidth
                                            type="submit"
                                            loading={loading}
                                        >
                                            {t('common.create')}
                                        </Button>
                                    </Grid>
                                </Grid>
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </form>
        </>
    );
}

export const dynamic = 'force-static';
export const dynamicParams = true;

export default NewObject;