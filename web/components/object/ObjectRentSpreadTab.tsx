import {
    GetObjectDocument,
    RentExpenseDataFragment,
    useDeleteRentExpenseMutation
} from "@/generated/graphql";
import {Button, Card, CardContent, Grid} from "@mui/joy";
import EntityList from "@/components/EntityList";
import {useMemo, useState} from "react";
import {GridColDef, GridRenderCellParams} from "@mui/x-data-grid";
import {useTranslation} from "next-export-i18n";
import {BarChart} from "@mui/x-charts";
import RentExpenseActionModal from "@/components/object/modal/RentExpenseActionModal";
import {useSearchParams} from "next/navigation";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";

interface ObjectRentSpreadTabProps {
    expenses: RentExpenseDataFragment[];
}

const ObjectRentSpreadTab = ({expenses}: ObjectRentSpreadTabProps) => {

    const {t} = useTranslation();
    const objectId = parseInt(useSearchParams().get('id') ?? '-1', 10);

    const [selectedExpense, setSelectedExpense] = useState<RentExpenseDataFragment|undefined>(undefined);
    const [addModal, setAddModal] = useState<boolean>(false);

    const [deleteMutation] = useDeleteRentExpenseMutation({
        refetchQueries: [
            {
                query: GetObjectDocument,
                variables: {id: objectId}
            }
        ]
    });

    const cols = useMemo<GridColDef[]>(() => [
        {
            field: 'name',
            headerName: t('object.expense.name'),
            width: 150
        },
        {
            field: 'value',
            headerName: t('object.expense.value')
        },
        {
            field: 'type',
            headerName: t('object.expense.type'),
            valueFormatter: (value) => t(`object.expense.types.${value}`)
        },
        {
            field: '_actions',
            headerName: t('common.actions'),
            width: 400,
            renderCell: ({row}: GridRenderCellParams) => (
                <Grid container direction="row" spacing={2}>
                    <Grid>
                        <Button onClick={() => setSelectedExpense(row)}>
                            <EditIcon />
                        </Button>
                    </Grid>
                    <Grid>
                        <Button onClick={async () => await deleteMutation({variables: {id: row.id}})} color="danger">
                            <DeleteIcon />
                        </Button>
                    </Grid>
                </Grid>
            )
        }
    ], [t]);

    return (
        <Grid container direction="row" spacing={2}>
            <Grid xs={12}>
                <Card>
                    <CardContent>
                        <Grid container direction="row">
                            <Grid xs={3}>
                                <Button
                                    variant="soft"
                                    color="primary"
                                    onClick={() => setAddModal(true)}
                                >{t('common.add')}</Button>
                            </Grid>
                        </Grid>
                    </CardContent>
                </Card>
            </Grid>
            <Grid xs={8}>
                <Card>
                    <CardContent>
                        <EntityList
                            columns={cols}
                            rows={expenses}
                            configPresetKey="expenses"
                        />
                        {(selectedExpense || addModal) && (
                            <RentExpenseActionModal
                                objectId={objectId}
                                onClose={() => {
                                    setSelectedExpense(undefined);
                                    setAddModal(false);
                                }}
                                expense={selectedExpense}
                            />
                        )}
                    </CardContent>
                </Card>
            </Grid>
            <Grid xs={4}>
                <Card>
                    <CardContent>
                        <BarChart
                            series={expenses.map((expense) => ({
                                data: [expense.value!],  stack: 'A', label: `${expense.name} (${t(`object.expense.types.${expense.type}`)})`
                            }))}
                            height={350}
                            legend={{hidden: true}}
                        />
                    </CardContent>
                </Card>
            </Grid>
        </Grid>
    );
}

export default ObjectRentSpreadTab;