import {OverridableComponent} from "@mui/material/OverridableComponent";
import {SvgIconTypeMap} from "@mui/material/SvgIcon/SvgIcon";
import DashboardIcon from '@mui/icons-material/Dashboard';
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import {RouteConfigType} from "@/typings/routeConfig";

const routes: RouteConfigType[] = [
    {
        name: 'Dashboard',
        path: '/dashboard',
        icon: <DashboardIcon />
    },
    {
        name: 'Settings',
        path: '/settings',
        icon: <SettingsRoundedIcon />
    }
];

export default routes;