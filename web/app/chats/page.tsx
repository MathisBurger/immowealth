"use client";
import {Typography} from "@mui/joy";
import {useTranslation} from "next-export-i18n";


const ChatsPage = () => {

    const {t} = useTranslation();

    return (
        <>
            <Typography level="h1">{t('common.chats')}</Typography>
        </>
    );
}

export default ChatsPage;