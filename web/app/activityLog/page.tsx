'use client';
import EntityList from "@/components/EntityList";
import {useMemo, useState} from "react";
import {GridColDef, GridPaginationModel} from "@mui/x-data-grid";
import {useGetLogDataQuery} from "@/generated/graphql";
import {useTranslation} from "next-export-i18n";


const ActivityLogPage = () => {

    const [model, setModel] = useState<GridPaginationModel>({
        page: 0,
        pageSize: 25
    });

    const {data, loading} = useGetLogDataQuery({
        variables: {pageNr: model.page, pageSize: model.pageSize}
    });

    const {t} = useTranslation();

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'id',
            headerName: 'ID'
        },
        {
            field: 'message',
            headerName: t('common.message'),
            width: 650
        },
        {
            field: 'date',
            headerName: t('common.date'),
            width: 200
        }
    ], [t])


    return (
        <EntityList
            columns={cols}
            rows={data?.logEntries ?? []}
            loading={loading}
            pagination
            paginationMode="server"
            paginationModel={model}
            onPaginationModelChange={setModel}
            rowCount={data?.logEntriesCount ?? 0}
            pageSizeOptions={[25, 50, 100]}
        />
    )
}

export default ActivityLogPage;