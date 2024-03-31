import {createContext, useContext} from "react";
import {SettingDataFragment} from "@/generated/graphql";


export const SettingsContext = createContext<SettingDataFragment[]>([]);

const useSettings = () => {
    const settings = useContext(SettingsContext);

    const findSetting = (key: string): string => {
        return settings.filter((s) => s.key === key)[0].value ?? '';
    }

    return {findSetting};
}

export default useSettings;