import {useMemo} from "react";
import {FormControl, FormLabel, Input, Option, Select, Stack, Typography} from "@mui/joy";
import {
    GetAllSettingsDocument,
    SettingDataFragment,
    SettingOptionDataFragment,
    useUpdateSettingMutation
} from "@/generated/graphql";
import useSnackbar from "@/hooks/useSnackbar";
import SettingOption from "@/components/settings/SettingOption";
import {useTranslation} from "next-export-i18n";

interface SettingsTabProps {
    dataObj: any;
}

const SettingsTab = ({dataObj}: SettingsTabProps) => {

    const snackbar = useSnackbar();
    const {t} = useTranslation();

    const [updateSettingMutation] = useUpdateSettingMutation({
        refetchQueries: [
            {
                query: GetAllSettingsDocument
            }
        ]
    });

    const updateSetting = async (key: string, value: string) => {
        if (value !== "null") {
            const result = await updateSettingMutation({
                variables: {
                    key: key,
                    value: value
                }
            });
            if (result.errors === undefined) {
                snackbar.success(t("settings.messages.update-successful"));
                let event = new Event("reRenderAll");
                document.dispatchEvent(event);
            }
        }
    }



    const sectionsObj = useMemo<object>(() => {
        let obj = {};
        for (const el of Object.values(dataObj)) {
            // @ts-ignore
            if(obj[`${el?.section}`] === undefined) {
                // @ts-ignore
                obj[`${el?.section}`] = {};
            }
            // @ts-ignore
            obj[`${el?.section}`][`${el?.key}`] = el;
        }
        return obj;
    }, [dataObj]);

    return (
        <Stack spacing={2}>
            {Object.entries(sectionsObj).map((data: [string, SettingDataFragment[]]) => (
                <>
                    <Typography level="h2">{t(`settings.section.${data[0]}`)}</Typography>
                    {Object.values(data[1]).map((el) => (
                        <FormControl key={el.key}>
                            <FormLabel>{t(`settings.${el.key}`)}</FormLabel>
                            {el.options ? (
                                <Select value={el.value} onChange={(_, c) => updateSetting(`${el.key}`, `${c}`)}>
                                    {el.options.map((option) => (
                                        <SettingOption option={option as SettingOptionDataFragment} key={option?.key} />
                                    ))}
                                </Select>
                            ) : (
                                <Input
                                    type="text"
                                    variant="soft"
                                    value={el.value ?? ''}
                                    onChange={(e) => updateSetting(`${el.key}`, `${e.target.value}`)}
                                />
                            )}
                        </FormControl>
                    ))}
                </>
            ))}
        </Stack>
    );
}

export default SettingsTab;