'use client';
import {Button, Card, CardContent, Divider, FormControl, FormLabel, Grid, Input, Stack, Typography} from "@mui/joy";
import {DatePicker} from "@mui/x-date-pickers";
import {useRouter} from "next/navigation";
import {FormEvent} from "react";
import {GetAllObjectsDocument, useCreateRealEstateMutation} from "@/generated/graphql";


const NewObject = () => {

    const router = useRouter();
    const [mutation, {loading}] = useCreateRealEstateMutation({
        refetchQueries: [{query: GetAllObjectsDocument}]
    });

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
                    zip: formData.get("zip") as string
                }}
        });
        if (result.errors === undefined) {
            router.push(`/objects/${result.data?.createRealEstate.id}`);
        } else {
            alert(result.errors[0].message);
        }
    }

    return (
        <>
            <Typography level="h1">Neues Objekt</Typography>
            <Divider />
            <form onSubmit={submit}>
                <Grid container direction="row" spacing={2}>
                    <Grid xs={6}>
                        <Card>
                            <CardContent>
                                <Stack spacing={2}>
                                    <Typography level="h3">Grunddaten</Typography>
                                    <FormControl>
                                        <FormLabel>Straße und Hausnummer</FormLabel>
                                        <Input
                                            required
                                            name="streetAndHouseNr"
                                            placeholder="Straße und Hausnummer"
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <Grid container direction="row" spacing={2}>
                                        <Grid xs={6}>
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
                                        </Grid>
                                        <Grid xs={6}>
                                            <FormControl>
                                                <FormLabel>Ort</FormLabel>
                                                <Input
                                                    required
                                                    name="city"
                                                    placeholder="Ort"
                                                    variant="soft"
                                                    fullWidth
                                                />
                                            </FormControl>
                                        </Grid>
                                    </Grid>
                                    <FormControl>
                                        <FormLabel>Kaufpreis</FormLabel>
                                        <Input
                                            required
                                            name="initialPrice"
                                            placeholder="Kaufpreis"
                                            variant="soft"
                                            type="number"
                                            fullWidth
                                            endDecorator={"€"}
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>Kaufdatum</FormLabel>
                                        <DatePicker disableFuture name="dateBought" />
                                    </FormControl>
                                </Stack>
                            </CardContent>
                        </Card>
                    </Grid>
                    <Grid xs={6}>
                        <Card>
                            <CardContent>
                                <Stack spacing={2}>
                                    <Typography level="h3">Kreditdaten</Typography>
                                    <FormControl>
                                        <FormLabel>Bank</FormLabel>
                                        <Input
                                            required
                                            name="bank"
                                            placeholder="Bank"
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>Kreditsumme</FormLabel>
                                        <Input
                                            required
                                            name="creditAmount"
                                            placeholder="Kreditsumme"
                                            variant="soft"
                                            type="number"
                                            fullWidth
                                            endDecorator={"€"}
                                        />
                                    </FormControl>
                                    <Grid container direction="row" spacing={2}>
                                        <Grid xs={6}>
                                            <FormControl>
                                                <FormLabel>Zins</FormLabel>
                                                <Input
                                                    required
                                                    name="interestRate"
                                                    placeholder="Zins"
                                                    variant="soft"
                                                    type="number"
                                                    fullWidth
                                                    endDecorator={"%"}
                                                />
                                            </FormControl>
                                        </Grid>
                                        <Grid xs={6}>
                                            <FormControl>
                                                <FormLabel>Tilgung</FormLabel>
                                                <Input
                                                    required
                                                    name="redemptionRate"
                                                    placeholder="Tilgung"
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
                                        <Typography level="h4">Aktionen</Typography>
                                    </Grid>
                                    <Grid xs={2}>
                                        <Button
                                            variant="solid"
                                            color="neutral"
                                            fullWidth
                                            onClick={() => router.back()}
                                        >
                                            Abbrechen
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
                                            Erstellen
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