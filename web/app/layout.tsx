'use client';
import { Inter } from "next/font/google";
import {ApolloClient, ApolloLink, ApolloProvider, HttpLink, InMemoryCache} from "@apollo/client";
import BaseLayout from "@/components/BaseLayout";
import {ReactNode} from "react";
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import ApolloBuilder from "@/components/ApolloBuilder";
import {SnackbarProvider} from "notistack";

const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({
  children,
}: Readonly<{
  children: ReactNode;
}>) {

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
