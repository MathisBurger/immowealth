import {HousePriceChangeDataFragment, useAllHousePriceChangesQuery} from "@/generated/graphql";
import {useMemo, useState} from "react";
import {Divider, Select, Typography, Option, Table} from "@mui/joy";


const HousingPrices = () => {

    const {data} = useAllHousePriceChangesQuery();

    const [filter, setFilter] = useState<string>('');

    const zips = useMemo<string[]>(() => {
        let all: string[] = [];
        for (let el of data?.allHousePricesChange ?? []) {
            if (!all.includes(el?.zip ?? '')) {
                all.push(el?.zip ?? '');
            }
        }
        return all;
    }, [data]);

    const filtered = useMemo<HousePriceChangeDataFragment[]>(
        () => (data?.allHousePricesChange ?? [])
            .filter((el) => filter === '' || el?.zip === filter) as HousePriceChangeDataFragment[],
    [filter, data]);

    return (
        <>
            <Typography level="h1">Preisänderungen</Typography>
            <Divider />
            <Select
                placeholder="Choose"
                variant="soft"
                value={filter}
                onChange={(_, f) => setFilter(f ?? '')}
            >
                {zips.map((op) => (
                    <Option value={op}>{op}</Option>
                ))}
            </Select>
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
                    <th>Änderung</th>
                    <th>Zip</th>
                    <th>Year</th>
                </tr>
                </thead>
                <tbody>
                {filtered.map((change) => (
                    <tr>
                        <td>{change.id}</td>
                        <td>{change.change}%</td>
                        <td>{change.zip}</td>
                        <td>{change.year}</td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </>
    )
}