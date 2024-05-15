'use client';
import {useSearchParams} from "next/navigation";
import useCurrentUser from "@/hooks/useCurrentUser";
import {useGetTenantLazyQuery} from "@/generated/graphql";
import {useEffect, useMemo, useState} from "react";
import {Button, Typography} from "@mui/joy";
import EntityList from "@/components/EntityList";
import {GridColDef} from "@mui/x-data-grid";
import {useTranslation} from "next-export-i18n";
import CreateTenantMemberModal from "@/components/tenant/CreateTenantMemberModal";
import UserRoles, {isGranted} from "@/utilts/userRoles";


const TenantPage = () => {
    const id = useSearchParams().get('id');
    const currentUser = useCurrentUser();
    const {t} = useTranslation();
    const [query,{data, loading}] = useGetTenantLazyQuery();
    const [modalOpen, setModalOpen] = useState<boolean>(false);

    const tenantId = useMemo<number>(
        () => id === null ? currentUser?.tenant?.id : parseInt(id, 10),
        [currentUser, id]
    );

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
            {isGranted(currentUser, [UserRoles.ADMIN]) && (
                <Button
                    color="primary"
                    variant="soft"
                    sx={{width: 200}}
                    onClick={() => setModalOpen(true)}
                >{t('tenant.createMember')}</Button>
            )}
            <EntityList
                columns={cols}
                rows={data?.tenant.users ?? []}
            />
            {modalOpen && (
                <CreateTenantMemberModal
                    tenantId={tenantId}
                    onClose={() => setModalOpen(false)}
                />
            )}
        </>
    );
}

export default TenantPage;