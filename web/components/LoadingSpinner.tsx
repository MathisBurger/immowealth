import {ReactNode} from "react";
import {CircularProgress} from "@mui/joy";

interface LoadingSpinnerProps {
    loading: boolean;
    children: ReactNode;
}

const LoadingSpinner = ({loading, children}: LoadingSpinnerProps) => {

    if (loading) {
        return <CircularProgress variant="plain" />;
    }

    return children as JSX.Element;
}

export default LoadingSpinner;