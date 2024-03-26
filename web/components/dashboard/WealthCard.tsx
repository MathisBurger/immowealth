import {Card, Grid, Typography} from "@mui/joy";
import {PieChart} from "@mui/x-charts";
import React from "react";
import {WealthResponse} from "@/generated/graphql";
import {formatNumber} from "@/utilts/formatter";
import useCurrencySymbol from "@/hooks/useCurrencySymbol";

interface WealthCardProps {
    /**
     * The wealth data
     */
    data?: WealthResponse;
    /**
     * The label of the card
     */
    label: string;
}

/**
 * Displays wealth situation in a card
 *
 * @constructor
 */
const WealthCard = ({data, label}: WealthCardProps) => {

    const currency = useCurrencySymbol();

    return (
        <Grid xs={6}>
            <Card size="sm" variant="soft">
                <Typography level="h4">
                    {label}
                </Typography>
                <Grid container direction="row" spacing={2}>
                    <Grid xs={6} alignItems="center">
                        <Typography level="h2">
                            {formatNumber(data?.total)}{currency}
                        </Typography>
                    </Grid>
                    <Grid xs={6}>
                        <PieChart
                            legend={{hidden: true}}
                            series={[
                                {
                                    //@ts-ignore
                                    data: data?.detailed ?? [],
                                    innerRadius: 70,
                                    outerRadius: 100
                                },
                            ]}
                            width={300}
                            height={200}
                            tooltip={{trigger: 'item'}}
                        />
                    </Grid>

                </Grid>
            </Card>
        </Grid>
    );
}

export default WealthCard;