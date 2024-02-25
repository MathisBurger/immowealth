import type { Metadata } from "next";
import { Inter } from "next/font/google";
import {ApolloClient, ApolloProvider, InMemoryCache} from "@apollo/client";
import BaseLayout from "@/components/BaseLayout";
import {ReactNode} from "react";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Immowealth",
  description: "Simple application to track your immo wealth",
};

const client = new ApolloClient({
    uri: 'http://localhost:8080/',
    cache: new InMemoryCache(),
});

export default function RootLayout({
  children,
}: Readonly<{
  children: ReactNode;
}>) {
  return (
    <html lang="de">
      <body className={inter.className}>
        <ApolloProvider client={client}>
            <BaseLayout children={children} />
        </ApolloProvider>
      </body>
    </html>
  );
}
