'use client';
import {useGetAllTenantsQuery} from "@/generated/graphql";
import {Button, Grid, Typography} from "@mui/joy";
import {useTranslation} from "next-export-i18n";
import EntityList from "@/components/EntityList";
import {useMemo} from "react";
import {GridColDef, GridRenderCellParams} from "@mui/x-data-grid";
import {useRouter} from "next/navigation";
import OpenInNewIcon from "@mui/icons-material/OpenInNew";
import DeleteIcon from "@mui/icons-material/Delete";


const TenantsPage = () => {

    const {data} = useGetAllTenantsQuery();
    const {t} = useTranslation();
    const router = useRouter();

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'id',
            headerName: 'ID'
        },
        {
            field: 'name',
            headerName: t('common.name'),
            width: 300
        },
        {
            field: 'users',
            headerName: t('common.users'),
            valueGetter: (value) => (value as any[]).length
        },
        {
            field: '_actions',
            headerName: t('common.actions'),
            width: 400,
            renderCell: ({row}: GridRenderCellParams) => (
                <Grid container direction="row" spacing={2}>
                    <Grid>
                        <Button onClick={() => router.push('/tenant?id=' + row.id)}>
                            <OpenInNewIcon />
                        </Button>
                    </Grid>
                </Grid>
            )
        }
    ], [t, router]);

    console.log(data)

    return (
        <>
            <Typography level="h1">{t('routes.tenants')}</Typography>
            <Button
                variant="soft"
                color="primary"
                onClick={() => router.push("/tenants/create")}
            >{t('tenant.new')}</Button>
            <EntityList
                columns={cols}
                rows={data?.allTenants ?? []}
            />
        </>
    );
}

export default TenantsPage;