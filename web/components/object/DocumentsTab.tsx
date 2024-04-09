import {UploadedFileFragment} from "@/generated/graphql";
import FileTreeItem from "@/components/FileTreeItem";
import {SimpleTreeView} from "@mui/x-tree-view";

interface DocumentsTabProps {
    docs: UploadedFileFragment[];
    objectId: number;
    refetch: () => void;
}

const DocumentsTab = ({docs, objectId, refetch}: DocumentsTabProps) => {

    return (
        <SimpleTreeView>
            <FileTreeItem docs={docs} objectId={objectId}  path="" refetch={refetch} />
        </SimpleTreeView>
    );
}

export default DocumentsTab;