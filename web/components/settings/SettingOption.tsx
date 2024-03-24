import {SettingOptionDataFragment} from "@/generated/graphql";
import {Option} from "@mui/joy";
import SettingOptionPrefixDisplay from "@/components/settings/SettingOptionPrefixDisplay";

interface SettingOptionProps {
    option: SettingOptionDataFragment;
}

const SettingOption = ({option}: SettingOptionProps) => {

    return (
        <Option value={option.value}>
            {option.iconPrefix && (
                <SettingOptionPrefixDisplay name={option.iconPrefix} />
            )}
            {option.value}
        </Option>
    );
}

export default SettingOption;