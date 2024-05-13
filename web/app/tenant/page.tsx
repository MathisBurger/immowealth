'use client';
import {useSearchParams} from "next/navigation";
import useCurrentUser from "@/hooks/useCurrentUser";
import {useGetTenantLazyQuery} from "@/generated/graphql";
import {useEffect, useMemo} from "react";
import {Grid, Typography} from "@mui/joy";
import EntityList from "@/components/EntityList";
import {GridColDef} from "@mui/x-data-grid";
import {useTranslation} from "next-export-i18n";


const TenantPage = () => {
    const id = useSearchParams().get('id');
    const currentUser = useCurrentUser();
    const {t} = useTranslation();
    const [query,{data, loading}] = useGetTenantLazyQuery();

    useEffect(() => {
        if (id !== null) {
            query({variables: {id: parseInt(id, 10)}});
        } else if (currentUser?.tenant?.id !== undefined && currentUser?.tenant?.id !== null) {
            query({variables: {id: currentUser?.tenant?.id}});
        }
    }, [id, currentUser]);

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'id',
            headerName: 'ID'
        },
        {
            field: 'username',
            headerName: t('common.name'),
            width: 200
        }
    ], []);

    return (
        <>
            <Typography level="h1">{data?.tenant.name}</Typography>
            <EntityList
                columns={cols}
                rows={data?.tenant.users ?? []}
            />
        </>
    );
}

export default TenantPage;