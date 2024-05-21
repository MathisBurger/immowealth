import {SimpleUserFragment, TenantFragment, useGetAllUsersQuery} from "@/generated/graphql";
import {Modal, ModalDialog, Typography} from "@mui/joy";
import {useTranslation} from "next-export-i18n";
import MoverList, {MoverListData} from "@/components/MoverList";
import {useEffect, useMemo, useState} from "react";
import {GridColDef} from "@mui/x-data-grid";


interface ManageTenantMembersModalProps {
    onClose: () => void;
    tenant: TenantFragment;
}

const ManageTenantMembersModal = ({onClose, tenant}: ManageTenantMembersModalProps) => {

    const {t} = useTranslation();
    const [data, setData] = useState<MoverListData<SimpleUserFragment>>({left: [], right: tenant.users as SimpleUserFragment[] ?? []});
    const {data: allUsers} = useGetAllUsersQuery();

    useEffect(() => {
        if (allUsers?.allUsers) {
            setData({...data, left: allUsers.allUsers as SimpleUserFragment[] ?? []});
        }
    }, [allUsers]);

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'id',
            headerName: 'ID',
        } ,
        {
            field: 'username',
            headerName: t('common.username')
        }
    ], [t]);

    return (
        <Modal open onClose={onClose} sx={{zIndex: 10001}}>
            <ModalDialog style={{width: '90vw', zIndex: 10001}} layout="center">
                <Typography level="h1">{t('tenant.manageMembers')}</Typography>
                <MoverList<SimpleUserFragment>
                    data={data}
                    cols={cols}
                    setData={setData}
                />
            </ModalDialog>
        </Modal>
    );
}

export default ManageTenantMembersModal;