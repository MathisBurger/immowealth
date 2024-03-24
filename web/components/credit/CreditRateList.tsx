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
import {useTranslation} from "next-export-i18n";

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

    const {t} = useTranslation();

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'id',
            headerName: 'ID'
        },
        {
            field: 'amount',
            headerName: t('credit.rate'),
            valueFormatter: ({value}: GridValueFormatterParams) => `${formatNumber(value ?? 0)}€`
        },
        {
            field: 'date',
            headerName: t('credit.bookingDate'),
            valueFormatter: ({value}: GridValueFormatterParams) => `${dayjs(value).format("DD.MM.YYYY")}`
        },
        {
            field: 'note',
            headerName: t('credit.note'),
            width: 400
        },
        {
            field: 'actions',
            headerName: t('common.actions'),
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