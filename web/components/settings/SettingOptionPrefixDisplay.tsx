import {SettingOptionPrefix} from "@/generated/graphql";
import ReactCountryFlag from "react-country-flag";
import {CurrencyPrefixValues, mapPrefix} from "@/hooks/useCurrencySymbol";


interface SettingOptionPrefixProps {
    name: string;
}

const SettingOptionPrefixDisplay = ({name}: SettingOptionPrefixProps) => {

    if (CurrencyPrefixValues.includes(name as SettingOptionPrefix)) {
        return mapPrefix(name);
    }

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