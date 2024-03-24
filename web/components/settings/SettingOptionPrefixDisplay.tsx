import {SettingOptionPrefix} from "@/generated/graphql";
import ReactCountryFlag from "react-country-flag";


interface SettingOptionPrefixProps {
    name: string;
}

const SettingOptionPrefixDisplay = ({name}: SettingOptionPrefixProps) => {

    switch (name) {
        case SettingOptionPrefix.DeFlag:
            return <ReactCountryFlag countryCode="DE" />
        case SettingOptionPrefix.EnFlag:
            return <ReactCountryFlag countryCode="US" />
        default:
            return <div />
    }
}

export default SettingOptionPrefixDisplay;