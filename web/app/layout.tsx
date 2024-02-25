import type { Metadata } from "next";
import { Inter } from "next/font/google";
import {Box, CssBaseline, CssVarsProvider} from "@mui/joy";
import PathDisplay from "@/components/PathDisplay";
import Sidebar from "@/components/Sidebar";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Immowealth",
  description: "Simple application to track your immo wealth",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="de">
      <body className={inter.className}>
      <CssVarsProvider disableTransitionOnChange>
        <CssBaseline />
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
      </CssVarsProvider>
      </body>
    </html>
  );
}
