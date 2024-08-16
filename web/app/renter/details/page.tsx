"use client";
import {useRouter, useSearchParams} from "next/navigation";
import {Card, CardContent, Divider, Grid, List, ListItem, ListItemButton, Typography} from "@mui/joy";
import {useGetRenterQuery} from "@/generated/graphql";
import {useTranslation} from "next-export-i18n";


const RenterDetails = () => {
    const id = useSearchParams().get('id') ?? '';
    const {data} = useGetRenterQuery({
        variables: {renterId: parseInt(id, 10)}
    });
    const {t} = useTranslation();
    const router = useRouter();

    return (
        <>
            <Typography level="h1">
                {data?.renter.firstName} {data?.renter.lastName}
            </Typography>
            <Grid container direction="row" spacing={2}>
                <Grid xs={4}>
                    <Card>
                        <CardContent>
                            <Typography level="h3">{t('renter.history')}</Typography>
                            <Divider />
                            <List>
                                {data?.renter.statistics?.history.map((realEstate) => (
                                    <ListItem key={realEstate?.id}>
                                        <ListItemButton onClick={() => router.push('/objects/details?id=' + realEstate?.id)}>
                                            {realEstate?.streetAndHouseNr} {realEstate?.zip} {realEstate?.city}
                                        </ListItemButton>
                                    </ListItem>
                                ))}
                            </List>
                        </CardContent>
                    </Card>
                </Grid>
                <Grid xs={8}>
                    <Typography level="h2">
                        More data coming soon
                    </Typography>
                </Grid>
            </Grid>
        </>
    )
}

export default RenterDetails;