import {Button, Table} from "@mui/joy";
import {
    CreditRateDataFragment,
    GetAllObjectsDocument,
    useDeleteCreditRateMutation,
    useDeleteRealEstateMutation
} from "@/generated/graphql";
import dayjs from "dayjs";
import DeleteIcon from "@mui/icons-material/Delete";
import {formatNumber} from "@/utilts/formatter";
import {useCallback, useMemo} from "react";
import {GridColDef, GridRenderCellParams, GridValueFormatterParams} from "@mui/x-data-grid";
import EntityList from "@/components/EntityList";

interface CreditRateListProps {
    /**
     * All credit rate elements
     */
    elements: CreditRateDataFragment[];
}

/**
 * List of all credit elements
 *
 * @constructor
 */
const CreditRateList = ({elements}: CreditRateListProps) => {

    const [deleteMutation, {loading: deleteLoading}] = useDeleteCreditRateMutation();

    const deleteObject = useCallback(async (id: string) => {
        await deleteMutation({
            variables: {id: parseInt(`${id}`)}
        });
    }, [deleteMutation]);

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'id',
            headerName: 'ID'
        },
        {
            field: 'amount',
            headerName: 'Rate',
            valueFormatter: ({value}: GridValueFormatterParams) => `${formatNumber(value ?? 0)}€`
        },
        {
            field: 'date',
            headerName: 'Buchungsdatum',
            valueFormatter: ({value}: GridValueFormatterParams) => `${dayjs(value).format("DD.MM.YYYY")}`
        },
        {
            field: 'note',
            headerName: 'Notiz',
            width: 400
        },
        {
            field: 'actions',
            headerName: 'Aktionen',
            renderCell: ({row}: GridRenderCellParams) => (
                <Button color="danger" onClick={() => deleteObject(`${row.id}`)}>
                    <DeleteIcon />
                </Button>
            )
        }
    ], [deleteObject]);

    return (
        <EntityList columns={cols} rows={elements} />
    );
}

export default CreditRateList;