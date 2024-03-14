import {DataGrid, DataGridProps, GridColDef, GridRowModel, GridRowsProp, GridToolbar} from "@mui/x-data-grid";
import {useMemo} from "react";

interface EntityListProps extends DataGridProps {}


const EntityList = ({
    rows,
    columns
}: EntityListProps) => {

    const iDedRows = useMemo<any[]>(
        () => rows.map((r, index) => ({...r, ghostId: index+1})),
        [rows]
    );

    return (
        <DataGrid
            columns={columns}
            rows={iDedRows}
            getRowId={(row) => row.ghostId}
            slots={{toolbar: GridToolbar}}
        />
    );
}

export default EntityList;