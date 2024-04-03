'use client';
import { Inter } from "next/font/google";
import BaseLayout from "@/components/BaseLayout";
import {ReactNode, useEffect, useState} from "react";
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import ApolloBuilder from "@/components/ApolloBuilder";
import {SnackbarProvider} from "notistack";
import {SettingDataFragment, useGetAllSettingsQuery} from "@/generated/graphql";
import LoadingSpinner from "@/components/LoadingSpinner";
import {SettingsContext} from "@/hooks/useSettings";

const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({
  children,
}: Readonly<{
  children: ReactNode;
}>) {

    const [reRender, setRerender] = useState<number>(0);

    useEffect(() => {
        document.addEventListener('reRenderAll', () => {
            setRerender(reRender+1);
        })
    });

  return (
    <html lang="de">
      <body className={inter.className}>
            <SnackbarProvider maxSnack={5} anchorOrigin={{ horizontal: "right", vertical: "bottom" }}>
                <ApolloBuilder>
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <BaseLayout>
                                {children}
                            </BaseLayout>
                    </LocalizationProvider>
                </ApolloBuilder>
            </SnackbarProvider>
      </body>
    </html>
  );
}

export const dynamic = 'force-static';
export const dynamicParams = true;
