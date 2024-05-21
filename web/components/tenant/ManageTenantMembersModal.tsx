import {SimpleUserFragment, TenantFragment} from "@/generated/graphql";
import {Modal, ModalDialog, Typography} from "@mui/joy";
import {useTranslation} from "next-export-i18n";


interface ManageTenantMembersModalProps {
    onClose: () => void;
    tenant: TenantFragment;
}

const ManageTenantMembersModal = ({onClose, tenant}: ManageTenantMembersModalProps) => {

    const {t} = useTranslation();
    return (
        <Modal open onClose={onClose}>
            <ModalDialog>
                <Typography level="h1">{t('tenant.manageMembers')}</Typography>
            </ModalDialog>
        </Modal>
    );
}

export default ManageTenantMembersModal;