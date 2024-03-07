import {Button, ButtonProps, CircularProgress} from "@mui/joy";


type LoadingButtonProps = Omit<ButtonProps, 'disabled'> & {loading: boolean};

const LoadingButton = (props: LoadingButtonProps) => {

    return (
        <Button
            disabled={props.loading}
            {...props}
        >
            {props.loading ? <CircularProgress variant="plain"/> : props.children}
        </Button>
    );
}

export default LoadingButton;