'use client';
import {GetObjectQuery} from "@/generated/graphql";
import {Card, CardContent, Grid, Table} from "@mui/joy";
import {LineChart} from "@mui/x-charts";
import LoadingSpinner from "@/components/LoadingSpinner";
import {formatNumber} from "@/utilts/formatter";
import {useMemo} from "react";
import {GridColDef, GridValueFormatterParams} from "@mui/x-data-grid";
import EntityList from "@/components/EntityList";
import {useTranslation} from "next-export-i18n";
import useCurrencySymbol from "@/hooks/useCurrencySymbol";

interface ObjectPriceChangesTabProps {
    /**
     * Data loading
     */
    loading: boolean;
    /**
     * The data
     */
    data: GetObjectQuery|undefined;
    /**
     * Field of the data that should be accessed
     */
    fieldToAccess: string;
}

/**
 * Displays a price change
 *
 * @constructor
 */
const ObjectPriceChangesTab = ({loading, data, fieldToAccess}: ObjectPriceChangesTabProps) => {

    const {t} = useTranslation();
    const currency = useCurrencySymbol();

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'value',
            headerName: t('common.price'),
            width: 200,
            valueFormatter: (value) => `${formatNumber(value)}${currency}`
        },
        {
            field: 'year',
            headerName: t('common.year'),
        }
    ], [t]);

    return (
        <LoadingSpinner loading={loading}>
            <Grid container direction="row" spacing={2}>
                <Grid xs={6}>
                    <Card>
                        <CardContent>
                            {/* @ts-ignore */}
                            <EntityList columns={cols} rows={data?.object[fieldToAccess]} configPresetKey={"objectPriceChanges_" + fieldToAccess} />
                        </CardContent>
                    </Card>
                </Grid>
                <Grid xs={6}>
                    <Card>
                        <CardContent>
                            <LineChart
                                series={[{
                                    // @ts-ignore
                                    data: data?.object[fieldToAccess].map((c) => c?.value ?? 0)
                                }]}
                                xAxis={[{
                                    // @ts-ignore
                                    data: data?.object[fieldToAccess].map((c) => c?.year ?? 0)
                                }]}
                                height={500}
                            />
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
        </LoadingSpinner>
    );
}

export default ObjectPriceChangesTab;