import {
    GetTenantDocument,
    SimpleUserFragment,
    TenantFragment,
    useGetAllUsersQuery,
    useMoveTenantMembersMutation
} from "@/generated/graphql";
import {Button, ButtonGroup, Modal, ModalDialog, Typography} from "@mui/joy";
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
    const [mutation] = useMoveTenantMembersMutation({
        refetchQueries: [
            {
                query: GetTenantDocument,
                variables: {id: tenant.id}
            }
        ]
    });
    const {data: allUsers} = useGetAllUsersQuery();

    useEffect(() => {
        if (allUsers?.allUsers) {
            setData({...data, left: allUsers.allUsers as SimpleUserFragment[] ?? []});
        }
    }, [allUsers]);

    const moveMembers = async () => {
        const response = await mutation({
            variables: {input: {
                    id: tenant.id,
                    users: data.right.map((e) => e.id)
                }}
        });
        if (response.errors === undefined) {
            onClose();
        }
    }

    return (
        <Modal open onClose={onClose} sx={{zIndex: 10001}}>
            <ModalDialog style={{width: '90vw', zIndex: 10001}} layout="center">
                <Typography level="h1">{t('tenant.manageMembers')}</Typography>
                <MoverList<SimpleUserFragment>
                    data={data}
                    fieldToAccess="username"
                    setData={setData}
                />
                <ButtonGroup>
                    <Button color="primary" variant="soft" onClick={moveMembers}>{t('tenant.moveMembers')}</Button>
                    <Button color="neutral" variant="soft" onClick={onClose}>{t('common.cancel')}</Button>
                </ButtonGroup>
            </ModalDialog>
        </Modal>
    );
}

export default ManageTenantMembersModal;