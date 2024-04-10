import DashboardIcon from '@mui/icons-material/Dashboard';
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import {RouteConfigType} from "@/typings/routeConfig";
import ApartmentIcon from '@mui/icons-material/Apartment';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import ShowChartIcon from '@mui/icons-material/ShowChart';
import MapIcon from '@mui/icons-material/Map';
import ChangeHistoryIcon from '@mui/icons-material/ChangeHistory';
import ArchiveIcon from '@mui/icons-material/Archive';

const routes: RouteConfigType[] = [
    {
        name: 'dashboard',
        path: '/dashboard',
        icon: <DashboardIcon />
    },
    {
        name: 'objects',
        path: '/objects',
        icon: <ApartmentIcon />
    },
    {
        name: 'credits',
        path: '/credits',
        icon: <AttachMoneyIcon />
    },
    {
        name: 'housingPrices',
        path: '/housingPrices',
        icon: <ShowChartIcon />
    },
    {
        name: 'map',
        path: '/map',
        icon: <MapIcon />
    },
    {
        name: 'activityLog',
        path: '/activityLog',
        icon: <ChangeHistoryIcon />
    },
    {
      name: 'archived',
      path: '/archived',
      icon: <ArchiveIcon />
    },
    {
        name: 'settings',
        path: '/settings',
        icon: <SettingsRoundedIcon />
    },
];

export default routes;