import {ReactNode} from "react";
import {CircularProgress} from "@mui/joy";

interface LoadingSpinnerProps {
    /**
     * Loading prop
     */
    loading: boolean;
    /**
     * children
     */
    children: ReactNode;
}

/**
 * Loading spinner wrapper
 *
 * @constructor
 */
const LoadingSpinner = ({loading, children}: LoadingSpinnerProps) => {

    if (loading) {
        return <CircularProgress variant="plain" />;
    }

    return children as JSX.Element;
}

export default LoadingSpinner;