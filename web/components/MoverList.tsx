import {Button, Grid} from "@mui/joy";
import {ArrowBack, ArrowForward} from "@mui/icons-material";
import {useState} from "react";
import MoverListDataList from "@/components/MoverListDataList";

type IdAble = {
    id: string|number|undefined;
}

export interface MoverListData<T> {
    left: T[];
    right: T[];
}

interface MoverListProps<T> {
    data: MoverListData<T>;
    setData: (data: MoverListData<T>) => void;
    fieldToAccess: string;
}

const MoverList = <T, >({data, setData, fieldToAccess}: MoverListProps<T>) => {

    const [leftSelection, setLeftSelection] = useState<T[]>([]);
    const [rightSelection, setRightSelection] = useState<T[]>([]);

    const moveRight = () => {
        console.log(leftSelection);
        if (leftSelection && leftSelection.length > 0) {
            const ids = leftSelection.map((e) => (e as IdAble).id);
            setData({
                left: data.left.filter((e) => ids.indexOf((e as IdAble).id) === -1),
                right: [...data.right, data.left.find((e) => ids.indexOf((e as IdAble).id) > -1) as T]
            })
        }
    }

    const moveLeft = () => {
        if (rightSelection && rightSelection.length > 0) {
            const ids = rightSelection.map((e) => (e as IdAble).id);
            setData({
                right: data.right.filter((e) => ids.indexOf((e as IdAble).id) === -1),
                left: [...data.left, data.right.find((e) => ids.indexOf((e as IdAble).id) > -1) as T]
            })
        }
    }


    return (
        <Grid container direction="row" alignItems="center" justifyContent="center">
            <Grid xs={5}>
                <MoverListDataList<T>
                    data={data.left}
                    selected={leftSelection}
                    setSelected={setLeftSelection}
                    fieldToAccess={fieldToAccess}
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
                <MoverListDataList<T>
                    data={data.right}
                    selected={rightSelection}
                    setSelected={setRightSelection}
                    fieldToAccess={fieldToAccess}
                />
            </Grid>
        </Grid>
    );
}

export default MoverList;