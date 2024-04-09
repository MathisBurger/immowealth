import {UploadedFileFragment} from "@/generated/graphql";
import FileTreeItem from "@/components/FileTreeItem";
import {SimpleTreeView} from "@mui/x-tree-view";

interface DocumentsTabProps {
    docs: UploadedFileFragment[];
    objectId: number;
}

const DocumentsTab = ({docs, objectId}: DocumentsTabProps) => {

    return (
        <SimpleTreeView>
            <FileTreeItem docs={docs} objectId={objectId} />
        </SimpleTreeView>
    );
}

export default DocumentsTab;