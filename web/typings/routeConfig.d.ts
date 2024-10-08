export interface RouteConfigType {
    name: string;
    path: string;
    icon?: JSX.Element;
    children?: RouteConfigType[];
    authorized: boolean;
    roles?: string[];
}