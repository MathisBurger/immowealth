import {GetObjectDocument, UploadedFileFragment, useDeleteFileMutation} from "@/generated/graphql";
import {useMemo, useState} from "react";
import {TreeItem} from "@mui/x-tree-view";
import FolderIcon from '@mui/icons-material/Folder';
import {Grid, IconButton, Input} from "@mui/joy";
import DownloadIcon from '@mui/icons-material/Download';
import DeleteIcon from '@mui/icons-material/Delete';
import AddBoxIcon from '@mui/icons-material/AddBox';
import {useTranslation} from "next-export-i18n";
import UploadFileModal from "@/components/object/modal/UploadFileModal";
import {useCookies} from "react-cookie";

interface FileTreeItemProps {
    docs: UploadedFileFragment[];
    objectId: number;
    refetch: () => void;
    path: string;
}

interface NestedHelper {
    path: string;
    elements: UploadedFileFragment[];
}

const FileTreeItem = ({docs, objectId, refetch, path}: FileTreeItemProps) => {

    const [cookies] = useCookies(['jwt']);
    const [deleteMutation] = useDeleteFileMutation({
        refetchQueries: [
            {
                query: GetObjectDocument,
                variables: {id: objectId}
            }
        ]
    });
    const [modalOpen, setModalOpen] = useState<boolean>(false);
    const [folderName, setFolderName] = useState<string|null>(null);
    const [clickedFolder, setClickedFolder] = useState<boolean>(false);

    const {t} = useTranslation();

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
        return arr;
    }, [folders, nonRootDocs]);

    const downloadFile = (id: number) => {
        const url = `${process.env.NODE_ENV === 'production' ? '/' : 'http://localhost:8080/'}file?id=${id}`;
        fetch(url, {
            headers: {
                'Authorization': 'Bearer ' + cookies.jwt
            }
        } )
            .then((response) => response.blob())
            .then((blob) => {
                const _url = window.URL.createObjectURL(blob);
                // @ts-ignore
                window.open(_url, "_blank").focus();
            }).catch((err) => {
            console.log(err);
        });
    }


    return (
        <>
            {foldersWithFiles.map((f, i) => (
                <TreeItem key={f.path + i + Math.random()} itemId={f.path + Math.random()*1000} label={
                    <Grid container direction="row">
                        <FolderIcon color="primary" sx={{marginRight: '5px'}} />
                        {f.path}
                    </Grid>
                }>
                    <FileTreeItem docs={f.elements} objectId={objectId} refetch={refetch} path={path === "" ? f.path : path + "/" + f.path} />
                </TreeItem>
            ))}
            {folderName && (
                <TreeItem key={path + folderName} itemId={path + folderName} label={
                    <Grid container direction="row">
                        <FolderIcon color="primary" sx={{marginRight: '5px'}} />
                        {folderName}
                    </Grid>
                }>
                    <FileTreeItem docs={[]} objectId={objectId} refetch={() => {setFolderName(null);refetch()}} path={(path === "" ? "" : path + "/") + folderName} />
                </TreeItem>
            )}
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
            <TreeItem itemId={"add" + Math.random()*100000} label={
                <Grid container direction="row">
                    <AddBoxIcon sx={{marginRight: '5px'}} />
                    {t('common.add')}
                </Grid>
            } onClick={() => setModalOpen(true)} />
            <TreeItem itemId={"folder" + Math.random()*1000000} label={
                <Grid container direction="row">
                    <FolderIcon sx={{marginRight: '5px'}} />
                    {clickedFolder ? (
                        <form onSubmit={(e) => {
                            e.preventDefault();
                            const formData = new FormData(e.currentTarget);
                            setFolderName(`${formData.get("txt")}`);
                            setClickedFolder(false);
                        }}>
                            <Input type="text" autoFocus={true} name="txt"/>
                        </form>
                    ) : t('common.add-folder')}
                </Grid>
            } onClick={() => setClickedFolder(true)} onKeyUp={(e) => e.key === 'Escape' ? setClickedFolder(false) : null} />
            {modalOpen && (
                <UploadFileModal objectId={objectId} onClose={() => {setModalOpen(false);setFolderName(null);}} refetch={() => {refetch()}} rootPath={path} />
            )}

        </>
    )
}

export default FileTreeItem;