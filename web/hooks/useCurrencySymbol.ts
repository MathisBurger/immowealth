import {SettingOptionPrefix} from "@/generated/graphql";
import {useContext} from "react";
import {SettingsContext} from "@/hooks/useSettings";

export const CurrencyPrefixValues = [
    SettingOptionPrefix.Usd,
    SettingOptionPrefix.Eur,
    SettingOptionPrefix.Gbp,
    SettingOptionPrefix.Jpy,
    SettingOptionPrefix.Chf,
    SettingOptionPrefix.Cad,
    SettingOptionPrefix.Sek,
    SettingOptionPrefix.Nok,
    SettingOptionPrefix.Cnh,
    SettingOptionPrefix.Dkk,
    SettingOptionPrefix.Aed,
    SettingOptionPrefix.Mxn,
    SettingOptionPrefix.Rub,
    SettingOptionPrefix.Try,
];

export const mapPrefix = (name: string) => {

    switch (name) {
        case SettingOptionPrefix.Usd:
            return "$";
        case SettingOptionPrefix.Eur:
            return "€";
        case SettingOptionPrefix.Gbp:
            return "GBP";
        case SettingOptionPrefix.Jpy:
            return "¥";
        case SettingOptionPrefix.Chf:
            return "CHF";
        case SettingOptionPrefix.Cad:
            return "CAD";
        case SettingOptionPrefix.Sek:
            return "SEK";
        case SettingOptionPrefix.Nok:
            return "NOK";
        case SettingOptionPrefix.Cnh:
            return "CNH";
        case SettingOptionPrefix.Dkk:
            return "DKK";
        case SettingOptionPrefix.Aed:
            return "AED";
        case SettingOptionPrefix.Mxn:
            return "MXN";
        case SettingOptionPrefix.Rub:
            return "RUB";
        case SettingOptionPrefix.Try:
            return "TRY";
    }
}


const useCurrencySymbol = () => {
    const settings = useContext(SettingsContext);
    const currency = settings.filter((s) => s.key === "currency")[0].value;
    return mapPrefix(currency!);
}

export default useCurrencySymbol;