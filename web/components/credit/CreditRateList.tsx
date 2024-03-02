import {Table} from "@mui/joy";
import {CreditRateDataFragment} from "@/generated/graphql";
import dayjs from "dayjs";

interface CreditRateListProps {
    elements: CreditRateDataFragment[];
}

const CreditRateList = ({elements}: CreditRateListProps) => {

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
            </tr>
            </thead>
            <tbody>
            {elements.map((object) => (
                <tr>
                    <td>{object.id}</td>
                    <td>{object.amount}€</td>
                    <td>{dayjs(object.date).format("DD.MM.YYYY")}</td>
                </tr>
            ))}
            </tbody>
        </Table>
    );
}

export default CreditRateList;