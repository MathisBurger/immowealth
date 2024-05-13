import DashboardIcon from '@mui/icons-material/Dashboard';
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import {RouteConfigType} from "@/typings/routeConfig";
import ApartmentIcon from '@mui/icons-material/Apartment';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import ShowChartIcon from '@mui/icons-material/ShowChart';
import MapIcon from '@mui/icons-material/Map';
import ChangeHistoryIcon from '@mui/icons-material/ChangeHistory';
import ArchiveIcon from '@mui/icons-material/Archive';
import GroupsIcon from '@mui/icons-material/Groups';
import UserRoles from "@/utilts/userRoles";

const routes: RouteConfigType[] = [
    {
        name: 'dashboard',
        path: '/dashboard',
        icon: <DashboardIcon />,
        authorized: true
    },
    {
        name: 'objects',
        path: '/objects',
        icon: <ApartmentIcon />,
        authorized: true
    },
    {
        name: 'credits',
        path: '/credits',
        icon: <AttachMoneyIcon />,
        authorized: true
    },
    {
        name: 'housingPrices',
        path: '/housingPrices',
        icon: <ShowChartIcon />,
        authorized: true
    },
    {
        name: 'map',
        path: '/map',
        icon: <MapIcon />,
        authorized: true
    },
    {
        name: 'activityLog',
        path: '/activityLog',
        icon: <ChangeHistoryIcon />,
        authorized: true
    },
    {
      name: 'archived',
      path: '/archived',
      icon: <ArchiveIcon />,
        authorized: true
    },
    {
        name: 'settings',
        path: '/settings',
        icon: <SettingsRoundedIcon />,
        authorized: true
    },
    {
        name: 'tenants',
        path: '/tenants',
        icon: <GroupsIcon />,
        authorized: true,
        roles: [UserRoles.ADMIN]
    },
    {
        name: 'tenant',
        path: '/tenant',
        icon: <GroupsIcon />,
        authorized: true,
        roles: [UserRoles.TENANT_ASSIGNED, UserRoles.TENANT_OWNER]
    }
];

export default routes;