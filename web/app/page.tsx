'use client';
import {Card, Divider, Grid, Typography} from "@mui/joy";
import React from 'react';
import {PieChart} from "@mui/x-charts";
import {useGetWealthQuery} from "@/generated/graphql";
import WealthCard from "@/components/dashboard/WealthCard";

const Dashboard = () => {

    const {data} = useGetWealthQuery();

  return (
      <React.Fragment>
          <Typography level="h1">Dashboard</Typography>
          <Divider />
          <Typography level="h2">Vermögen</Typography>
          <Grid container direction="row" spacing={2}>
              <WealthCard data={data?.grossWealthWithoutInflation} label="Brutto (ohne Wertsteigerung)" />
              <WealthCard data={data?.grossWealthWithInflation} label="Brutto (mit Wertsteigerung)" />
              <WealthCard data={data?.netWealthWithoutInflation} label="Netto (ohne Wertsteigerung)" />
              <WealthCard data={data?.netWealthWithInflation} label="Netto (mit Wertsteigerung)" />
          </Grid>
      </React.Fragment>
  )
};

export default Dashboard;
