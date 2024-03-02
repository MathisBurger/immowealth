'use client';
import {HousePriceChangeDataFragment, useAllHousePriceChangesQuery} from "@/generated/graphql";
import {useMemo, useState} from "react";
import {Divider, Select, Typography, Option, Table, Button} from "@mui/joy";
import {useRouter} from "next/navigation";


const HousingPrices = () => {

    const {data} = useAllHousePriceChangesQuery();
    const router = useRouter();

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
            <Button
                variant="solid"
                color="primary"
                sx={{width: '200px'}}
                onClick={() => router.push("/housingPrices/new")}
            >
                Neue Preisänderung
            </Button>
            <Divider />
            <Select
                placeholder="Choose"
                variant="soft"
                sx={{width: '200px'}}
                value={filter}
                onChange={(_, f) => setFilter(f ?? '')}
            >
                <Option value={''}>Kein Filter</Option>
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
    );
}

export default HousingPrices;