import { enqueueSnackbar } from 'notistack';

/**
 * Provides snackbar functionality
 */
const useSnackbar = () => {

    /**
     * Error snackbar
     *
     * @param message Error message
     */
    const error = (message: string) => {
        enqueueSnackbar(message, {variant: 'error'});
    }

    return {error};
}

export default useSnackbar;