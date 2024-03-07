import {Button, ButtonProps, CircularProgress} from "@mui/joy";


/**
 * Loading button props
 */
type LoadingButtonProps = Omit<ButtonProps, 'disabled'> & {loading: boolean};

/**
 * Loading button.
 *
 * @constructor
 */
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