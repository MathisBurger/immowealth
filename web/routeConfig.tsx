import DashboardIcon from '@mui/icons-material/Dashboard';
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import {RouteConfigType} from "@/typings/routeConfig";
import ApartmentIcon from '@mui/icons-material/Apartment';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';

const routes: RouteConfigType[] = [
    {
        name: 'Dashboard',
        path: '/dashboard',
        icon: <DashboardIcon />
    },
    {
        name: 'Objekte',
        path: '/objects',
        icon: <ApartmentIcon />
    },
    {
        name: 'Kredite',
        path: '/credits',
        icon: <AttachMoneyIcon />
    },
    {
        name: 'Einstellungen',
        path: '/settings',
        icon: <SettingsRoundedIcon />
    },
];

export default routes;