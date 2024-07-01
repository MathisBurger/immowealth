'use client';
import useCurrentUser from "@/hooks/useCurrentUser";
import UserRoles, {isGranted} from "@/utilts/userRoles";
import WealthCardDashboard from "@/components/dashboard/WealthCardDashboard";
import React from "react";
import {Divider, Grid, Typography} from "@mui/joy";
import WealthCard from "@/components/dashboard/WealthCard";
import {WealthResponse} from "@/generated/graphql";

const Dashboard = () => {

    const currentUser = useCurrentUser();

    if (isGranted(currentUser, [UserRoles.TENANT_ASSIGNED, UserRoles.TENANT_OWNER, UserRoles.ADMIN])) {
        return <WealthCardDashboard />
    }
    if (isGranted(currentUser, [UserRoles.RENTER])) {
        return (
            <React.Fragment>
                <Typography level="h1">Dashboard</Typography>
                <Divider />
                <Typography level="h2">Data will be added soon</Typography>
            </React.Fragment>
        )
    }
    return null;
};

export const dynamic = 'force-static';
export const dynamicParams = true;

export default Dashboard;
