import { enqueueSnackbar } from 'notistack';

const useSnackbar = () => {

    const error = (message: string) => {
        enqueueSnackbar(message, {variant: 'error'});
    }

    return {error};
}

export default useSnackbar;