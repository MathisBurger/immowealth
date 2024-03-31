'use client';
import {Button, Divider, FormControl, FormLabel, Input, Stack, Typography} from "@mui/joy";
import {AllHousePriceChangesDocument, useAddHousePriceChangeMutation} from "@/generated/graphql";
import {FormEvent} from "react";
import {useRouter} from "next/navigation";
import {useTranslation} from "next-export-i18n";

const NewHousingPriceChange = () => {

    const router = useRouter();
    const {t} = useTranslation();

    const [mutation, {loading}] = useAddHousePriceChangeMutation({
        refetchQueries: [
            {
                query: AllHousePriceChangesDocument
            }
        ]
    });

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        const result = await mutation({
            variables: {
                change: parseFloat(`${formData.get('change')}`),
                zip: `${formData.get('zip')}`,
                year: parseInt(`${formData.get('year')}`, 10)
            }
        });
        if (result.errors === undefined) {
            router.push('/housingPrices');
        }
    }

    return (
        <>
            <Typography level="h1">{t('housingPrices.new')}</Typography>
            <Divider />
            <form onSubmit={onSubmit}>
                <Stack spacing={2}>
                    <FormControl>
                        <FormLabel>{t('common.change')}</FormLabel>
                        <Input
                            required
                            name="change"
                            placeholder={t('common.change')}
                            variant="soft"
                            type="number"
                            fullWidth
                            endDecorator="%"
                        />
                    </FormControl>
                    <FormControl>
                        <FormLabel>{t('common.zip')}</FormLabel>
                        <Input
                            required
                            name="zip"
                            placeholder={t('common.zip')}
                            variant="soft"
                            fullWidth
                        />
                    </FormControl>
                    <FormControl>
                        <FormLabel>{t('common.year')}</FormLabel>
                        <Input
                            required
                            name="year"
                            placeholder={t('common.year')}
                            variant="soft"
                            type="number"
                            fullWidth
                        />
                    </FormControl>
                    <Button
                        variant="solid"
                        color="primary"
                        fullWidth
                        type="submit"
                        loading={loading}
                    >
                        {t('common.create')}
                    </Button>
                </Stack>
            </form>
        </>
    );
}

export const dynamic = 'force-static';
export const dynamicParams = true;

export default NewHousingPriceChange;