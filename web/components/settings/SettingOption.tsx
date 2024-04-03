import {SettingOptionDataFragment} from "@/generated/graphql";
import {Option} from "@mui/joy";
import SettingOptionPrefixDisplay from "@/components/settings/SettingOptionPrefixDisplay";
import {useTranslation} from "next-export-i18n";

interface SettingOptionProps {
    option: SettingOptionDataFragment;
}

const SettingOption = ({option}: SettingOptionProps) => {

    const {t} = useTranslation();

    return (
        <Option value={option.key}>
            {option.iconPrefix && (
                <SettingOptionPrefixDisplay name={option.iconPrefix} />
            )}
            &nbsp;
            {option.translationKey ? t(`settings.options.${option.translationKey}`) : option.value}
        </Option>
    );
}

export default SettingOption;