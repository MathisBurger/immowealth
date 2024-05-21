import {GridColDef, GridRowSelectionModel} from "@mui/x-data-grid";
import {Button, Grid} from "@mui/joy";
import EntityList from "@/components/EntityList";
import {ArrowBack, ArrowForward} from "@mui/icons-material";
import {useState} from "react";

type IdAble = {
    id: string|number|undefined;
}

export interface MoverListData<T> {
    left: T[];
    right: T[];
}

interface MoverListProps<T> {
    data: MoverListData<T>;
    cols: GridColDef[];
    setData: (data: MoverListData<T>) => void;
}

const MoverList = <T, >({data, setData, cols}: MoverListProps<T>) => {

    const [leftSelection, setLeftSelection] = useState<GridRowSelectionModel|undefined>(undefined);
    const [rightSelection, setRightSelection] = useState<GridRowSelectionModel|undefined>(undefined);

    const moveRight = () => {
        console.log(leftSelection);
        if (leftSelection && leftSelection.length > 0) {
            setData({
                left: data.left.filter((e) => (e as IdAble).id !== leftSelection[0]),
                right: [...data.right, data.left.find((e) => (e as IdAble).id === leftSelection[0]) as T]
            })
        }
    }

    const moveLeft = () => {
        if (rightSelection && rightSelection.length > 0) {
            setData({
                right: data.right.filter((e) => (e as IdAble).id !== rightSelection[0]),
                left: [...data.left, data.right.find((e) => (e as IdAble).id === rightSelection[0]) as T]
            })
        }
    }


    return (
        <Grid container direction="row" alignItems="center" justifyContent="center">
            <Grid xs={5}>
                <EntityList
                    columns={cols}
                    rows={data.left}
                    onRowSelectionModelChange={setLeftSelection}
                    rowSelection
                    rowSelectionModel={leftSelection}
                />
            </Grid>
            <Grid xs={2} container direction="row" alignItems="center" justifyContent="center">
                <Grid xs={5}>
                    <Button onClick={moveLeft}>
                        <ArrowBack />
                    </Button>
                </Grid>
                <Grid xs={5}>
                    <Button onClick={moveRight}>
                        <ArrowForward />
                    </Button>
                </Grid>
            </Grid>
            <Grid xs={5}>
                <EntityList
                    columns={cols}
                    rows={data.right}
                    onRowSelectionModelChange={setRightSelection}
                    rowSelection
                    rowSelectionModel={rightSelection}
                />
            </Grid>
        </Grid>
    );
}

export default MoverList;