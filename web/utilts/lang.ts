

export const formatLang = (lang: string) => {
    switch (lang) {
        case "Deutsch":
            return "de";
        case "English":
            return "en";
        default:
            return "en";
    }
}