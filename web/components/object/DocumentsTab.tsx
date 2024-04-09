import {UploadedFileFragment} from "@/generated/graphql";
import FileTreeItem from "@/components/FileTreeItem";
import {SimpleTreeView} from "@mui/x-tree-view";

interface DocumentsTabProps {
    docs: UploadedFileFragment[];
}

const DocumentsTab = ({docs}: DocumentsTabProps) => {

    return (
        <SimpleTreeView>
            <FileTreeItem docs={docs} />
        </SimpleTreeView>
    );
}

export default DocumentsTab;