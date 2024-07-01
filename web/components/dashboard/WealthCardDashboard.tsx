import {useGetWealthQuery, WealthResponse} from "@/generated/graphql";
import React from "react";
import {Divider, Grid, Typography} from "@mui/joy";
import WealthCard from "@/components/dashboard/WealthCard";


const WealthCardDashboard = () => {


    const {data} = useGetWealthQuery();

    return (
        <React.Fragment>
            <Typography level="h1">Dashboard</Typography>
            <Divider />
            <Typography level="h2">Vermögen</Typography>
            <Grid container direction="row" spacing={2}>
                <WealthCard data={data?.grossWealthWithoutInflation as WealthResponse} label="Brutto (ohne Wertsteigerung)" />
                <WealthCard data={data?.grossWealthWithInflation as WealthResponse} label="Brutto (mit Wertsteigerung)" />
                <WealthCard data={data?.netWealthWithoutInflation as WealthResponse} label="Netto (ohne Wertsteigerung)" />
                <WealthCard data={data?.netWealthWithInflation as WealthResponse} label="Netto (mit Wertsteigerung)" />
            </Grid>
        </React.Fragment>
    )
}

export default WealthCardDashboard;