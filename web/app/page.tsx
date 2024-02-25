'use client';
import {Card, Divider, Grid, Typography} from "@mui/joy";
import React from 'react';
import {PieChart} from "@mui/x-charts";
import {useGetWealthQuery} from "@/generated/graphql";

const Dashboard = () => {

    const {data} = useGetWealthQuery();

  return (
      <React.Fragment>
          <Typography level="h1">Dashboard</Typography>
          <Divider />
          <Typography level="h2">Vermögen</Typography>
          <Grid container direction="row" spacing={2}>
              <Grid xs={6}>
                  <Card size="sm" variant="soft">
                      <Typography level="h4">
                          Brutto (ohne Wertsteigerung)
                      </Typography>
                      <Grid container direction="row" spacing={2}>
                          <Grid xs={6} alignItems="center">
                              <Typography level="h2">
                                  {data?.grossWealthWithoutInflation}€
                              </Typography>
                          </Grid>
                          <Grid xs={6}>
                              <PieChart
                                  legend={{hidden: true}}
                                  series={[
                                      {
                                          data: [
                                              { id: 0, value: 10, label: 'series A' },
                                              { id: 1, value: 15, label: 'series B' },
                                              { id: 2, value: 20, label: 'series C' },
                                          ],
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
              <Grid xs={6}>
                  <Card size="sm" variant="soft">
                      <Typography level="h4">
                          Brutto (mit Wertsteigerung)
                      </Typography>
                      <Grid container direction="row" spacing={2} justifyContent="space-between">
                          <Grid xs={6}>
                              <Typography level="h2">
                                  {data?.grossWealthWithInflation}€
                              </Typography>
                          </Grid>
                          <Grid xs={6}>
                              <PieChart
                                  legend={{hidden: true}}
                                  series={[
                                      {
                                          data: [
                                              { id: 0, value: 10, label: 'series A' },
                                              { id: 1, value: 15, label: 'series B' },
                                              { id: 2, value: 20, label: 'series C' },
                                          ],
                                          innerRadius: 70,
                                          outerRadius: 100
                                      },
                                  ]}
                                  width={300}
                                  height={200}
                                  tooltip={{trigger: 'none'}}
                              />
                          </Grid>

                      </Grid>
                  </Card>
              </Grid>
              <Grid xs={6}>
                  <Card size="sm" variant="soft">
                      <Typography level="h4">
                          Netto (ohne Wertsteigerung)
                      </Typography>
                      <Grid container direction="row" spacing={2}>
                          <Grid xs={6} alignItems="center">
                              <Typography level="h2">
                                  {data?.netWealthWithoutInflation}€
                              </Typography>
                          </Grid>
                          <Grid xs={6}>
                              <PieChart
                                  legend={{hidden: true}}
                                  series={[
                                      {
                                          data: [
                                              { id: 0, value: 10, label: 'series A' },
                                              { id: 1, value: 15, label: 'series B' },
                                              { id: 2, value: 20, label: 'series C' },
                                          ],
                                          innerRadius: 70,
                                          outerRadius: 100
                                      },
                                  ]}
                                  width={300}
                                  height={200}
                                  tooltip={{trigger: 'none'}}
                              />
                          </Grid>

                      </Grid>
                  </Card>
              </Grid>
              <Grid xs={6}>
                  <Card size="sm" variant="soft">
                      <Typography level="h4">
                          Netto (mit Wertsteigerung)
                      </Typography>
                      <Grid container direction="row" spacing={2} justifyContent="space-between">
                          <Grid xs={6}>
                              <Typography level="h2">
                                  {data?.netWealthWithoutInflation}€
                              </Typography>
                          </Grid>
                          <Grid xs={6}>
                              <PieChart
                                  legend={{hidden: true}}
                                  series={[
                                      {
                                          data: [
                                              { id: 0, value: 10, label: 'series A' },
                                              { id: 1, value: 15, label: 'series B' },
                                              { id: 2, value: 20, label: 'series C' },
                                          ],
                                          innerRadius: 70,
                                          outerRadius: 100
                                      },
                                  ]}
                                  width={300}
                                  height={200}
                                  tooltip={{trigger: 'none'}}
                              />
                          </Grid>

                      </Grid>
                  </Card>
              </Grid>
          </Grid>
      </React.Fragment>
  )
};

export default Dashboard;
