import {DropzoneArea} from "react-mui-dropzone";
import {useState} from "react";
import {Button, Grid, Modal, ModalDialog} from "@mui/joy";
import {useTranslation} from "next-export-i18n";

interface UploadFileModalProps {
    objectId: number;
    rootPath: string;
    onClose: () => void;
    refetch: () => void;
}

const UploadFileModal = ({objectId, onClose, rootPath, refetch}: UploadFileModalProps) => {

    const {t} = useTranslation();
    const [files, setFiles] = useState<File[]>([]);

    const onSubmit = async () => {
        for (let file of files) {
            const form = new FormData();
            form.append("file", file);
            form.append("fileName", file.name);
            form.append("fileRoot", rootPath);
            form.append("objectId", `${objectId}`);
            const options = {
                method: 'POST',
                //headers: {'Content-Type': 'multipart/form-data;'},
                body: form
            };
            await fetch(`${process.env.NODE_ENV === 'production' ? '' : 'http://localhost:8080'}/file/realEstateObject`, options);
        }
        refetch();
        onClose();
    }

    return (
        <Modal open onClose={onClose} sx={{zIndex: 10001}}>
            <ModalDialog style={{width: '80vw', zIndex: 10001}}>
                <DropzoneArea onChange={setFiles} filesLimit={20} />
                <Grid container direction="row" spacing={2} justifyContent="flex-end">
                    <Grid>
                        <Button
                            variant="outlined"
                            color="neutral"
                            onClick={onClose}
                        >
                            {t('common.cancel')}
                        </Button>
                    </Grid>
                    <Grid>
                        <Button variant="solid" color="primary" onClick={onSubmit}>
                            {t('common.save')}
                        </Button>
                    </Grid>
                </Grid>
            </ModalDialog>
        </Modal>
    )
}

export default UploadFileModal;