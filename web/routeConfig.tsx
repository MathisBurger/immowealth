import {OverridableComponent} from "@mui/material/OverridableComponent";
import {SvgIconTypeMap} from "@mui/material/SvgIcon/SvgIcon";
import DashboardIcon from '@mui/icons-material/Dashboard';
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import {RouteConfigType} from "@/typings/routeConfig";
import ApartmentIcon from '@mui/icons-material/Apartment';

const routes: RouteConfigType[] = [
    {
        name: 'Dashboard',
        path: '/dashboard',
        icon: <DashboardIcon />
    },
    {
        name: 'Einstellungen',
        path: '/settings',
        icon: <SettingsRoundedIcon />
    },
    {
        name: 'Objekte',
        path: '/objects',
        icon: <ApartmentIcon />
    }
];

export default routes;