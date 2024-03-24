'use client';
import {Box, CssBaseline, CssVarsProvider} from "@mui/joy";
import Sidebar from "@/components/Sidebar";
import PathDisplay from "@/components/PathDisplay";
import {ReactNode, useEffect} from "react";
import NoSSR from "@/components/NoSSR";
import useSettings, { SettingsContext } from "@/hooks/useSettings";
import {formatLang} from "@/utilts/lang";
import {useTranslation} from "next-export-i18n";
import {SettingDataFragment, useGetAllSettingsQuery} from "@/generated/graphql";

interface BaseLayoutProps {
    children: ReactNode;
}

/**
 * Base layout
 *
 * @constructor
 */
const BaseLayout = ({children}: BaseLayoutProps) => {
    const {data, loading} = useGetAllSettingsQuery();

    useEffect(() => {
        if (data?.allSettings) {
            let lang = (data?.allSettings ?? []).filter((s) => s?.key === "language")[0]?.value!;
            window.localStorage.setItem("next-export-i18n-lang", formatLang(lang));
            const event = new Event("localStorageLangChange");
            document.dispatchEvent(event);
        }
    }, [data?.allSettings]);

    return (
        <NoSSR>
            <SettingsContext.Provider value={(data?.allSettings ?? []) as SettingDataFragment[]}>
                <Box sx={{ display: 'flex', minHeight: '100dvh' }}>
                    <Sidebar />
                    <Box
                        component="main"
                        className="MainContent"
                        sx={{
                            px: { xs: 2, md: 6 },
                            pt: {
                                xs: 'calc(12px + var(--Header-height))',
                                sm: 'calc(12px + var(--Header-height))',
                                md: 3,
                            },
                            pb: { xs: 2, sm: 2, md: 3 },
                            flex: 1,
                            display: 'flex',
                            flexDirection: 'column',
                            minWidth: 0,
                            height: '100dvh',
                            gap: 1,
                        }}
                    >
                        <PathDisplay />
                        {children}
                    </Box>
                </Box>
            </SettingsContext.Provider>
        </NoSSR>
    );
}

export default BaseLayout;