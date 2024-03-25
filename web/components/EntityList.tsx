import {DataGrid, DataGridProps, GridColDef, GridRowModel, GridRowsProp, GridToolbar} from "@mui/x-data-grid";
import {useMemo} from "react";

interface EntityListProps extends DataGridProps {}


const EntityList = ({
    rows,
    columns,
    pagination,
    paginationMode,
    pageSizeOptions,
    paginationModel,
    onPaginationModelChange,
    rowCount
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
            paginationModel={paginationModel}
            pagination={pagination}
            pageSizeOptions={pageSizeOptions ?? [25, 50, 100]}
            paginationMode={paginationMode}
            onPaginationModelChange={onPaginationModelChange}
            rowCount={rowCount}
        />
    );
}

export default EntityList;