'use client';
import {useRouter} from "next/navigation";
import {
    Button,
    Card,
    CardContent,
    Checkbox,
    Divider,
    FormControl,
    FormLabel,
    Grid,
    Input,
    Stack,
    Typography
} from "@mui/joy";
import {useTranslation} from "next-export-i18n";
import {GetAllTenantsDocument, useCreateTenantMutation} from "@/generated/graphql";
import {FormEvent} from "react";


const CreateTenantPage = () => {

    const router = useRouter();
    const [mutation, {loading}] = useCreateTenantMutation({
        refetchQueries: [
            {
                query: GetAllTenantsDocument
            }
        ]
    });
    const {t} = useTranslation();

    const submit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        const resp = await mutation({
            variables: {
                input: {
                    username: `${formData.get("username")}`,
                    email: `${formData.get("email")}`,
                    password: `${formData.get("password")}`,
                    name: `${formData.get("name")}`
                }
            }
        });
        if (resp.errors === undefined) {
            router.push(`/tenant?id=${resp.data?.createTenant.id}`)
        }
    }

    return (
        <>
            <Typography level="h1">{t('tenant.new')}</Typography>
            <Divider />
            <form onSubmit={submit}>
                <Grid container direction="row" spacing={2}>
                    <Grid xs={12}>
                        <Card>
                            <CardContent>
                                <Stack spacing={2}>
                                    <Typography level="h2">{t('tenant.baseData')}</Typography>
                                    <FormControl>
                                        <FormLabel>{t('tenant.name')} *</FormLabel>
                                        <Input
                                            required
                                            name="name"
                                            placeholder={t('tenant.name')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <Typography level="h2">{t('tenant.owner')}</Typography>
                                    <FormControl>
                                        <FormLabel>{t('common.username')} *</FormLabel>
                                        <Input
                                            required
                                            name="username"
                                            placeholder={t('common.username')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('common.email')} *</FormLabel>
                                        <Input
                                            required
                                            name="email"
                                            placeholder={t('common.email')}
                                            variant="soft"
                                            fullWidth
                                        />
                                    </FormControl>
                                    <FormControl>
                                        <FormLabel>{t('common.password')} *</FormLabel>
                                        <Input
                                            required
                                            name="password"
                                            placeholder={t('common.password')}
                                            variant="soft"
                                            type="password"
                                            fullWidth
                                        />
                                    </FormControl>
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
    )
}

export default CreateTenantPage;