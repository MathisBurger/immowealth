import {useMemo, useState} from "react";
import {
    Button,
    ButtonGroup,
    FormControl,
    FormLabel,
    Grid,
    IconButton,
    Input,
    Option,
    Select,
    Switch,
    Typography
} from "@mui/joy";
import EditIcon from '@mui/icons-material/Edit';
import CloseIcon from '@mui/icons-material/Close';
import DoneIcon from '@mui/icons-material/Done';

export enum InputType {
    TEXT,
    NUMBER,
    BOOLEAN,
    SELECT
}

export interface DisplayOption {
    label: string;
    id: string|number;
}

export type DisplayValue = string|number|boolean|null|undefined;

interface EditableDisplayProps {
    inputType: InputType;
    value: DisplayValue;
    onChange: (val: DisplayValue) => Promise<boolean>;
    loading: boolean;
    options?: DisplayOption[];
    customDisplay?: (val: DisplayValue) => DisplayValue;
    fieldName? : string;
}

const EditableDisplay = ({inputType, value, onChange, loading, options, customDisplay, fieldName}: EditableDisplayProps) => {

    const [editing, setEditing] = useState<boolean>(false);
    const [hovered, setHovered] = useState<boolean>(false);

    const [internalValue, setInternalValue] = useState<DisplayValue>(value);

    const update = async () => {
        const result = await onChange(internalValue);
        if (result) {
            setEditing(false);
            setHovered(false);
        }
    }

    const inputField = useMemo(() => {
        switch (inputType) {
            case InputType.TEXT:
                return (
                    <FormControl>
                        <FormLabel>{fieldName}</FormLabel>
                        <Input type="text" variant="outlined" value={internalValue as string} onChange={(e) => setInternalValue(e.target.value)} />
                    </FormControl>
                );
            case InputType.NUMBER:
                return (
                    <FormControl>
                        <FormLabel>{fieldName}</FormLabel>
                        <Input type="number" variant="outlined" value={internalValue as number} onChange={(e) => setInternalValue(parseFloat(`${e.target.value}`))} />
                    </FormControl>
                );
            case InputType.BOOLEAN:
                return (
                    <Typography endDecorator={<Switch
                        variant="solid"
                        checked={internalValue as boolean}
                        onChange={(event) => setInternalValue(event.target.checked)}
                    />}>
                        {fieldName}
                    </Typography>
                );
            case InputType.SELECT:
                return (
                  <FormControl>
                      <FormLabel>{fieldName}</FormLabel>
                      <Select value={internalValue} onChange={(_, v) => setInternalValue(v)}>
                          {(options ?? []).map((el) => (
                              <Option value={el.id} key={el.id}>{el.label}</Option>
                          ))}
                      </Select>
                  </FormControl>
                );
        }
    }, [inputType, internalValue, options, fieldName])

    if (!editing) {
        return (
            <div
                onMouseEnter={() => setHovered(true)}
                onMouseLeave={() => setHovered(false)}
                style={{width: 'fit-content'}}
            >
                <Typography
                    endDecorator={hovered ? (
                        <Button
                            color="neutral"
                            onClick={() => setEditing(true)}
                            size="sm"
                            variant="soft"
                        ><EditIcon /></Button>
                    ) : undefined}
                >{customDisplay ? customDisplay(value) : value}</Typography>
            </div>
        );
    }

    return (
      <Grid container direction="row" spacing={2} sx={{marginTop: '5px', marginBottom: '5px'}} alignItems="center">
          {inputField}
          <ButtonGroup size="sm" sx={{height: '1em', transform: 'translateY(-0.3em)'}}>
              <IconButton color="danger" onClick={() => setEditing(false)} loading={loading}><CloseIcon /></IconButton>
              <IconButton color="success" onClick={update} loading={loading}><DoneIcon /></IconButton>
          </ButtonGroup>
      </Grid>
    );
}

export default EditableDisplay;