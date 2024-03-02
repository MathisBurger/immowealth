'use client';
import {Button, Divider, FormControl, FormLabel, Input, Stack, Typography} from "@mui/joy";
import {AllHousePriceChangesDocument, useAddHousePriceChangeMutation} from "@/generated/graphql";
import {FormEvent} from "react";
import {useRouter} from "next/navigation";

const NewHousingPriceChange = () => {

    const router = useRouter();

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
            <Typography level="h1">Neue Preisänderung</Typography>
            <Divider />
            <form onSubmit={onSubmit}>
                <Stack spacing={2}>
                    <FormControl>
                        <FormLabel>Änderung</FormLabel>
                        <Input
                            required
                            name="change"
                            placeholder="Änderung"
                            variant="soft"
                            type="number"
                            fullWidth
                            endDecorator="%"
                        />
                    </FormControl>
                    <FormControl>
                        <FormLabel>Postleitzahl</FormLabel>
                        <Input
                            required
                            name="zip"
                            placeholder="Postleitzahl"
                            variant="soft"
                            fullWidth
                        />
                    </FormControl>
                    <FormControl>
                        <FormLabel>Jahr</FormLabel>
                        <Input
                            required
                            name="year"
                            placeholder="Jahr"
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
                        Erstellen
                    </Button>
                </Stack>
            </form>
        </>
    );
}

export default NewHousingPriceChange;