"use client";
import {Button, Card, CardContent, FormControl, FormLabel, Input, Sheet, Typography} from "@mui/joy";
import {FormEvent, useMemo, useState} from "react";
import {useTranslation} from "next-export-i18n";
import {useRouter, useSearchParams} from "next/navigation";
import useSnackbar from "@/hooks/useSnackbar";

const ResetPasswordPage = () => {

    const {t} = useTranslation();
    const [checkInboxMsg, setCheckInboxMsg] = useState<boolean>(false);
    const searchParams = useSearchParams();
    const router = useRouter();
    const snackbar = useSnackbar();

    const session = useMemo<string|null>(() => searchParams.get("session"), [searchParams]);

    const onResetRequest = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const data = new FormData(e.currentTarget);
        await fetch(`${process.env.NODE_ENV === 'production' ? '' : 'http://localhost:8080'}/api/auth/resetPassword`, {
            method: "POST",
            body: JSON.stringify({
                username: `${data.get("username")}`,
                other: "13"
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        setCheckInboxMsg(true);
    }

    const onUpdate = async (e: FormEvent<HTMLFormElement>) => {
        const data = new FormData(e.currentTarget);
        const resp = await fetch(`${process.env.NODE_ENV === 'production' ? '' : 'http://localhost:8080'}/api/auth/resetPassword`, {
            method: "POST",
            body: JSON.stringify({
                password: `${data.get("password")}`,
                other: "13"
            }),
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + session
            }
        });
        if (resp.ok) {
            router.push("/login");
        } else {
            snackbar.error(resp.statusText);
        }
    }

    if (session) {
        return (
            <form onSubmit={onUpdate}>
                <Sheet
                    sx={{
                        width: 300,
                        mx: 'auto',
                        my: 4,
                        py: 3,
                        px: 2,
                        display: 'flex',
                        flexDirection: 'column',
                        gap: 2,
                        borderRadius: 'sm',
                        boxShadow: 'md',
                    }}
                    variant="outlined"
                >
                    <FormControl>
                        <FormLabel>Password</FormLabel>
                        <Input
                            name="password"
                            type="password"
                            required
                            placeholder="password"
                        />
                    </FormControl>
                    <Button sx={{mt: 1}} type="submit">{t('common.resetPassword')}</Button>
                </Sheet>
            </form>
        );
    }

    if (checkInboxMsg) {
        return (
            <Card color="success" variant="soft">
                <CardContent>
                    {t('common.pleaseCheckYourInbox')}
                </CardContent>
            </Card>
        )
    }

    return (
        <form onSubmit={onResetRequest}>
            <Sheet
                sx={{
                    width: 300,
                    mx: 'auto',
                    my: 4,
                    py: 3,
                    px: 2,
                    display: 'flex',
                    flexDirection: 'column',
                    gap: 2,
                    borderRadius: 'sm',
                    boxShadow: 'md',
                }}
                variant="outlined"
            >
                <FormControl>
                    <FormLabel>Username</FormLabel>
                    <Input
                        name="username"
                        type="username"
                        required
                        placeholder="username"
                    />
                </FormControl>
                <Button sx={{mt: 1}} type="submit">{t('common.requestPasswordReset')}</Button>
            </Sheet>
        </form>
    )
}

export default ResetPasswordPage;