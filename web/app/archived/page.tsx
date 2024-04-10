'use client';
import EntityList from "@/components/EntityList";
import {useMemo} from "react";
import {GridColDef, GridRenderCellParams} from "@mui/x-data-grid";
import {useTranslation} from "next-export-i18n";
import {GetAllArchivedDocument, useGetAllArchivedQuery, useUnarchiveEntityMutation} from "@/generated/graphql";
import {Button, ButtonGroup} from "@mui/joy";
import OpenInNewIcon from "@mui/icons-material/OpenInNew";
import {useRouter} from "next/navigation";


const ArchivedPage = () => {

    const {data} = useGetAllArchivedQuery();
    const {t} = useTranslation();
    const router = useRouter();
    const [mutation] = useUnarchiveEntityMutation({
        refetchQueries: [
            {
                query: GetAllArchivedDocument
            }
        ]
    });

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'value',
            headerName: t('common.value'),
            width: 500
        },
        {
            field: 'entityName',
            headerName: t('common.entityName'),
            width: 150,
            valueFormatter: (value, row) => {
                let spl = (row.entityName as string).split('.');
                return spl[spl.length-1];
            }
        },
        {
            field: '_actions',
            headerName: t('common.actions'),
            width: 300,
            renderCell: ({row}: GridRenderCellParams) => (
                <ButtonGroup sx={{marginTop: '7px'}}>
                    {row.directUrl ? (
                        <Button onClick={() => router.push(row.directUrl)}>
                            <OpenInNewIcon />
                        </Button>
                    ) : null}
                    <Button color="danger" onClick={() => {
                        mutation({variables: {id: row.entityID, entityName: row.entityName}})
                    }}>{t('common.unarchive')}</Button>
                </ButtonGroup>
            )
        }
    ], [t, router]);

    return (
        <EntityList columns={cols} rows={data?.allArchivedEntities ?? []} />
    )
}

export default ArchivedPage;