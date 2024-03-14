'use client';
import {useGetCreditsQuery} from "@/generated/graphql";
import {Button, Divider, Grid, Table, Typography} from "@mui/joy";
import OpenInNewIcon from "@mui/icons-material/OpenInNew";
import {useRouter} from "next/navigation";
import MapsHomeWorkIcon from '@mui/icons-material/MapsHomeWork';
import {formatNumber} from "@/utilts/formatter";
import {useMemo} from "react";
import {GridColDef, GridRenderCellParams, GridValueFormatterParams, GridValueGetterParams} from "@mui/x-data-grid";
import EntityList from "@/components/EntityList";

const CreditsPage = () => {

    const {data} = useGetCreditsQuery();
    const router = useRouter();

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'id',
            headerName: 'ID',
            valueGetter: ({row}: GridValueGetterParams) => row.credit.id,
        },
        {
            field: 'amount',
            headerName: 'Summe',
            width: 200,
            valueGetter: ({row}: GridValueGetterParams) => row.credit.amount,
            valueFormatter: ({value}: GridValueFormatterParams) => `${formatNumber(value)}€`
        },
        {
            field: 'creditRateSum',
            headerName: 'Getilgt',
            width: 200,
            valueFormatter: ({value}: GridValueFormatterParams) => `${formatNumber(value)}€`
        },
        {
            field: 'interestRate',
            headerName: 'Zins',
            width: 100,
            valueGetter: ({row}: GridValueGetterParams) => row.credit.interestRate,
            valueFormatter: ({value}: GridValueFormatterParams) => `${formatNumber(value)}%`
        },
        {
            field: 'redemptionRate',
            headerName: 'Tilgung',
            width: 100,
            valueGetter: ({row}: GridValueGetterParams) => row.credit.redemptionRate,
            valueFormatter: ({value}: GridValueFormatterParams) => `${formatNumber(value)}%`
        },
        {
            field: 'bank',
            headerName: 'Bank',
            width: 200,
            valueGetter: ({row}: GridValueGetterParams) => row.credit.bank,
        },
        {
            field: 'actions',
            headerName: 'Aktionen',
            width: 400,
            renderCell: ({row}: GridRenderCellParams) => (
                <Grid container direction="row" spacing={2}>
                    <Grid xs={6}>
                        <Button onClick={() => router.push('/credits/details?id=' + row.credit.id)}>
                            <OpenInNewIcon />
                        </Button>
                    </Grid>
                    <Grid xs={6}>
                        <Button onClick={() => router.push('/objects/details?id=' + row.realEstateObjectId)}>
                            <MapsHomeWorkIcon />
                        </Button>
                    </Grid>
                </Grid>
            )
        }
    ], [router]);

    return (
        <>
            <Typography level="h1">Kredite</Typography>
            <Divider />
            <EntityList
                rows={data?.allCredits ?? []}
                columns={cols}
            />
        </>
    );
}

export const dynamic = 'force-static';
export const dynamicParams = true;

export default CreditsPage;