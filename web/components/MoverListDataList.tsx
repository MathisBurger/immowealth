import {Checkbox, List, ListItem} from "@mui/joy";
import {useMemo} from "react";

type IdAble = {
    id: string|number|undefined;
}

interface MoverListDataListProps<T> {
    data: T[];
    selected: T[];
    setSelected: (d: T[]) => void;
    fieldToAccess: string;
}

const MoverListDataList = <T,>({data, selected, setSelected, fieldToAccess}: MoverListDataListProps<T>) => {

    const selectedIds = useMemo<number[]|string[]>(() => selected.map((e) => (e as any).id), [selected]);

    return (
        <List variant="outlined" sx={{borderRadius: 'sm'}}>
            {data.map((d, i) => (
                <ListItem key={`${d}_${i}_${typeof data}`}>
                    {/* @ts-ignore */}
                    <Checkbox checked={selectedIds.indexOf((d as IdAble).id) > -1}
                        onChange={(e) => {
                            if (e.target.checked) {
                                setSelected([...selected, d]);
                            } else {
                                setSelected(selected.filter((e) => (e as IdAble).id !== (d as IdAble).id));
                            }
                        }}
                    />
                    {(d as any)[fieldToAccess]}
                </ListItem>
            ))}
        </List>
    );
}

export default MoverListDataList;