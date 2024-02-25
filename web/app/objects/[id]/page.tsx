'use client';
import {useParams} from "next/navigation";
import {Divider, Typography} from "@mui/joy";


const ObjectDetailsPage = () => {

    const {id} = useParams<{id: string}>();
    return (
        <>
            <Typography level="h1">
                Objekt {id}
            </Typography>
            <Divider />
        </>
    );
}

export default ObjectDetailsPage;