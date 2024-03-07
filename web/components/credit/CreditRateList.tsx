import {Button, Table} from "@mui/joy";
import {
    CreditRateDataFragment,
    GetAllObjectsDocument,
    useDeleteCreditRateMutation,
    useDeleteRealEstateMutation
} from "@/generated/graphql";
import dayjs from "dayjs";
import DeleteIcon from "@mui/icons-material/Delete";

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

    const deleteObject = async (id: string) => {
        await deleteMutation({
            variables: {id: parseInt(`${id}`)}
        });
    }

    return (
        <Table
            borderAxis="x"
            size="lg"
            stickyHeader
            stripe="even"
            variant="soft"
        >
            <thead>
            <tr>
                <th>ID</th>
                <th>Rate</th>
                <th>Buchungsdatum</th>
                <th>Aktionen</th>
            </tr>
            </thead>
            <tbody>
            {elements.map((object) => (
                <tr key={object.id}>
                    <td>{object.id}</td>
                    <td>{object.amount}€</td>
                    <td>{dayjs(object.date).format("DD.MM.YYYY")}</td>
                    <td>
                        <Button color="danger" onClick={() => deleteObject(`${object.id}`)}>
                            <DeleteIcon />
                        </Button>
                    </td>
                </tr>
            ))}
            </tbody>
        </Table>
    );
}

export default CreditRateList;