import {GetObjectDocument, UploadedFileFragment, useDeleteFileMutation} from "@/generated/graphql";
import {useMemo} from "react";
import {TreeItem} from "@mui/x-tree-view";
import FolderIcon from '@mui/icons-material/Folder';
import {Grid, IconButton} from "@mui/joy";
import DownloadIcon from '@mui/icons-material/Download';
import DeleteIcon from '@mui/icons-material/Delete';

interface FileTreeItemProps {
    docs: UploadedFileFragment[];
    objectId: number;
}

interface NestedHelper {
    path: string;
    elements: UploadedFileFragment[];
}

const FileTreeItem = ({docs, objectId}: FileTreeItemProps) => {

    const [deleteMutation] = useDeleteFileMutation({
        refetchQueries: [
            {
                query: GetObjectDocument,
                variables: {id: objectId}
            }
        ]
    });

    const rootDocs = useMemo<UploadedFileFragment[]>(
        () => docs.filter((d) => d.fileRoot!.split("/").length === 1 || d.fileRoot === ""),
        [docs]
    );
    const nonRootDocs = useMemo<UploadedFileFragment[]>(
        () => docs.filter((d) => d.fileRoot!.split("/").length > 1),
        [docs]
    );

    const folders = useMemo<string[]>(
        () => nonRootDocs.map((d) => d.fileRoot!.split("/")[0]).filter((el, pos, self) => self.indexOf(el) === pos),
        [nonRootDocs]
    );
    
    const foldersWithFiles = useMemo<NestedHelper[]>(() => {
        let arr: NestedHelper[] = folders.map((f) => ({path: f, elements: []}));
        for (let doc of nonRootDocs) {
            for (let arrEl of arr) {
                if (doc.fileRoot!.startsWith(arrEl.path)) {
                    const ayo= {
                        ...doc,
                        fileRoot: doc.fileRoot!.replaceAll(arrEl.path + "/", "")
                    }
                    arrEl.elements.push(ayo);
                    break;
                }
            }
        }
        console.log(arr);
        return arr;
    }, [folders, nonRootDocs]);

    const downloadFile = (id: number) => {
        window.open(`${process.env.NODE_ENV === 'production' ? '/' : 'http://localhost:8080/'}file?id=${id}`, '_blank');
    }


    return (
        <>
            {foldersWithFiles.map((f, i) => (
                <TreeItem key={f.path + i} itemId={f.path} label={
                    <Grid container direction="row">
                        <FolderIcon color="primary" sx={{marginRight: '5px'}} />
                        {f.path}
                    </Grid>
                }>
                    <FileTreeItem docs={f.elements} objectId={objectId} />
                </TreeItem>
            ))}
            {rootDocs.map((d, i) => (
                <TreeItem key={d.fileName! + i} itemId={d.fileRoot! + d.fileName!} label={
                    <Grid container direction="row">
                        <Grid xs={11}>
                            {d.fileName!}
                        </Grid>
                        <Grid xs={1}>
                            <IconButton size="sm" onClick={() => downloadFile(d.id)}>
                                <DownloadIcon fontSize="small" />
                            </IconButton>
                            <IconButton size="sm" onClick={() => deleteMutation({variables: {id: d.id}})}>
                                <DeleteIcon fontSize="small" color="error" />
                            </IconButton>
                        </Grid>
                    </Grid>
                } />
            ))}
        </>
    )
}

export default FileTreeItem;