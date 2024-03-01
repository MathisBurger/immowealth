import {Button, ButtonProps, CircularProgress} from "@mui/joy";


type LoadingButtonProps = Omit<ButtonProps, 'disabled'> & {loading: boolean};

const LoadingButton = (props: LoadingButtonProps) => {

    return (
        <Button
            disabled={props.loading}
            children={
                props.loading ? <CircularProgress variant="plain" /> : props.children
            }
            {...props}
        />
    );
}

export default LoadingButton;