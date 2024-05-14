'use client';
import {
    DataGrid,
    DataGridProps,
    GridColDef, GridColumnVisibilityModel, GridDensity,
    GridFilterModel, GridRenderCellParams,
    GridRowModel,
    GridRowsProp, GridSortModel,
    GridToolbar
} from "@mui/x-data-grid";
import {useEffect, useMemo, useState} from "react";
import useConfigPresets from "@/hooks/useConfigPresets";
import {useTranslation} from "next-export-i18n";

interface EntityListProps extends DataGridProps {
    configPresetKey?: string;
}


const EntityList = ({
    rows,
    columns,
    pagination,
    paginationMode,
    pageSizeOptions,
    paginationModel,
    onPaginationModelChange,
    rowCount,
    configPresetKey
}: EntityListProps) => {

    const {data, loading, findByKey, saveConfigPreset} = useConfigPresets();
    const {t} = useTranslation();

    const baseConfigPresetKey = useMemo<string>(() => configPresetKey ?? "", [configPresetKey]);

    useEffect(() => {
        const filterModel = findByKey(baseConfigPresetKey + '_filterModel');
        if (filterModel) {
            setFilterModel(JSON.parse(filterModel.jsonString ?? "{}"));
        }
        const columnVisibilityModel = findByKey(baseConfigPresetKey + '_columnVisibility');
        if (columnVisibilityModel) {
            setColumnVisibilityModel(JSON.parse(columnVisibilityModel.jsonString ?? "{}"));
        }
        const density = findByKey(baseConfigPresetKey + '_density');
        if (density) {
            // @ts-ignore
            setDensity(density.jsonString ?? 'standard');
        }
        const sortModel = findByKey(baseConfigPresetKey + '_sortModel');
        if (sortModel) {
            setSortModel(JSON.parse(sortModel.jsonString ?? "{}"));
        }
    }, [data, baseConfigPresetKey]);

    const iDedRows = useMemo<any[]>(
        () => (rows ?? []).map((r, index) => ({...r, ghostId: index+1})),
        [rows]
    );

    const containsArchivedField = useMemo<boolean>(
        () => iDedRows.length > 0 ? iDedRows[0]?.archived !== undefined : false,
        [iDedRows]
    );

    const updatedCols = useMemo<readonly GridColDef[]>(() => {
        if (containsArchivedField) {
            return columns.concat({
                field: 'archived',
                headerName: t('common.archived'),
                renderCell: ({value}: GridRenderCellParams) => value ? t('common.yes') : t('common.no')
            });
        }
        return columns;
    }, [columns, containsArchivedField, t])

    const defaultFilterModel = useMemo<GridFilterModel|undefined>(() => {

        if (containsArchivedField) {
            return {"items":[{"field":"archived","operator":"equals","value":"false"}]} as GridFilterModel;
        }
        return undefined;
    }, [containsArchivedField]);

    const [filterModel, setFilterModel] = useState<GridFilterModel|undefined>(defaultFilterModel);
    const [columnVisibilitModel, setColumnVisibilityModel] = useState<GridColumnVisibilityModel|undefined>(undefined);
    const [density, setDensity] = useState<GridDensity|undefined>(undefined);
    const [sortModel, setSortModel] = useState<GridSortModel|undefined>(undefined);

    const updateFilterModel = async (model: GridFilterModel, details: any) => {
        await saveConfigPreset(JSON.stringify(model), baseConfigPresetKey + '_filterModel');
        setFilterModel(model);
    }

    const updateColumnVisibilityModel = async (model: GridColumnVisibilityModel, details: any) => {
        await saveConfigPreset(JSON.stringify(model), baseConfigPresetKey + '_columnVisibility');
        setColumnVisibilityModel(model);
    }

    const updateDensity = async (den: GridDensity) => {
        await saveConfigPreset(den, baseConfigPresetKey + '_density');
        setDensity(den);
    }

    const updateSortModel = async (model: GridSortModel, details: any) => {
        await saveConfigPreset(JSON.stringify(model), baseConfigPresetKey + '_sortModel');
        setSortModel(model);
    }

    return (
        <DataGrid
            columns={updatedCols}
            rows={iDedRows}
            getRowId={(row) => row.ghostId}
            slots={{toolbar: GridToolbar}}
            paginationModel={paginationModel}
            pagination={pagination ?? true}
            pageSizeOptions={pageSizeOptions ?? [25, 50, 100]}
            paginationMode={paginationMode ?? "client"}
            onPaginationModelChange={onPaginationModelChange}
            rowCount={rowCount ?? iDedRows.length}
            loading={loading}
            filterModel={filterModel}
            onFilterModelChange={updateFilterModel}
            columnVisibilityModel={columnVisibilitModel}
            onColumnVisibilityModelChange={updateColumnVisibilityModel}
            density={density}
            onDensityChange={updateDensity}
            sortModel={sortModel}
            onSortModelChange={updateSortModel}
        />
    );
}

export default EntityList;