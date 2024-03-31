import {
    ConfigPresetDataFragment, GetAllConfigPresetsForPathnameDocument,
    useCreateOrUpdateConfigPresetMutation,
    useGetAllConfigPresetsForPathnameLazyQuery
} from "@/generated/graphql";
import {useEffect, useState} from "react";
import useSnackbar from "@/hooks/useSnackbar";


const useConfigPresets = () => {
    const [mutation] = useCreateOrUpdateConfigPresetMutation();
    const [query, {data, loading}] = useGetAllConfigPresetsForPathnameLazyQuery();
    const snackbar = useSnackbar();

    useEffect(() => {
        if (document) {
            query({
                variables: {
                    pathname: document.location.href
                }
            });
        }
    }, []);

    const saveConfigPreset = async (jsonContent: string, key?: string) => {
        await mutation({
            variables: {
                jsonContent: jsonContent,
                key: key,
                pathname: document.location.href
            },
            refetchQueries: [
                {
                    query: GetAllConfigPresetsForPathnameDocument,
                    variables: {
                        pathname: document.location.href
                    }
                }
            ]
        });
    }

    const findByKey = (key: string): ConfigPresetDataFragment|null => {
        // @ts-ignore
        const filter: ConfigPresetDataFragment[] = (data?.allConfigPresetsForPathname ?? []).filter((c) => c?.key === key);
        if (filter.length !== 1) {
            return null;
        }
        return filter[0];
    }

    return {data, loading, findByKey, saveConfigPreset};
}

export default useConfigPresets;