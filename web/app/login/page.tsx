"use client";
import {Button, FormControl, FormLabel, Input, Link, Sheet, Typography} from "@mui/joy";
import {FormEvent} from "react";
import {useRouter} from "next/navigation";
import useSnackbar from "@/hooks/useSnackbar";
import {useTranslation} from "next-export-i18n";

const LoginPage = () => {

    const router = useRouter();
    const snackbar = useSnackbar();
    const {t} = useTranslation();

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const data = new FormData(e.currentTarget);
        const resp = await fetch(`${process.env.NODE_ENV === 'production' ? '' : 'http://localhost:8080'}/api/auth/login`, {
            method: 'POST',
            body: JSON.stringify({
                username: `${data.get("username")}`,
                password: `${data.get("password")}`
            }),
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: "include"
        });
        if (resp.status === 200) {
            router.push("/dashboard");
        } else {
            snackbar.error("Login failed");
        }
    }

    return (
        <form onSubmit={onSubmit}>
            <Sheet
                sx={{
                    width: 300,
                    mx: 'auto', // margin left & right
                    my: 4, // margin top & bottom
                    py: 3, // padding top & bottom
                    px: 2, // padding left & right
                    display: 'flex',
                    flexDirection: 'column',
                    gap: 2,
                    borderRadius: 'sm',
                    boxShadow: 'md',
                }}
                variant="outlined"
            >
                <div>
                    <Typography level="h4" component="h1">
                        <b>Welcome to Immowealth!</b>
                    </Typography>
                    <Typography level="body-sm">Sign in to continue.</Typography>
                </div>
                <FormControl>
                    <FormLabel>Username</FormLabel>
                    <Input
                        name="username"
                        type="username"
                        placeholder="username"
                    />
                </FormControl>
                <FormControl>
                    <FormLabel>Password</FormLabel>
                    <Input
                        name="password"
                        type="password"
                        placeholder="password"
                    />
                </FormControl>
                <Link href="/resetPassword">{t('common.resetPassword')}</Link>
                <Button sx={{mt: 1 /* margin top */}} type="submit">Log in</Button>
            </Sheet>
        </form>
    );
}

export default LoginPage;