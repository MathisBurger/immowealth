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
import {Chat} from '@mui/icons-material';
import SportsBarIcon from '@mui/icons-material/SportsBar';
import UserRoles from "@/utilts/userRoles";

const routes: RouteConfigType[] = [
    {
        name: 'dashboard',
        path: '/dashboard',
        icon: <DashboardIcon />,
        authorized: true,
        roles: [UserRoles.ADMIN, UserRoles.TENANT_OWNER, UserRoles.TENANT_ASSIGNED, UserRoles.RENTER]
    },
    {
        name: 'objects',
        path: '/objects',
        icon: <ApartmentIcon />,
        authorized: true,
        roles: [UserRoles.ADMIN, UserRoles.TENANT_OWNER, UserRoles.TENANT_ASSIGNED]
    },
    {
        name: 'credits',
        path: '/credits',
        icon: <AttachMoneyIcon />,
        authorized: true,
        roles: [UserRoles.ADMIN, UserRoles.TENANT_OWNER, UserRoles.TENANT_ASSIGNED]
    },
    {
        name: 'housingPrices',
        path: '/housingPrices',
        icon: <ShowChartIcon />,
        authorized: true,
        roles: [UserRoles.ADMIN, UserRoles.TENANT_OWNER, UserRoles.TENANT_ASSIGNED]
    },
    {
        name: 'map',
        path: '/map',
        icon: <MapIcon />,
        authorized: true,
        roles: [UserRoles.ADMIN, UserRoles.TENANT_OWNER, UserRoles.TENANT_ASSIGNED]
    },
    {
        name: 'chats',
        path: '/chats',
        icon: <Chat />,
        authorized: true,
        roles: [UserRoles.ADMIN, UserRoles.TENANT_OWNER, UserRoles.TENANT_ASSIGNED, UserRoles.RENTER]
    },
    {
        name: 'activityLog',
        path: '/activityLog',
        icon: <ChangeHistoryIcon />,
        authorized: true,
        roles: [UserRoles.ADMIN]
    },
    {
      name: 'archived',
      path: '/archived',
      icon: <ArchiveIcon />,
      authorized: true,
      roles: [UserRoles.ADMIN, UserRoles.TENANT_OWNER, UserRoles.TENANT_ASSIGNED]
    },
    {
        name: 'simpleRentabilityCalculation',
        path: '/simpleRentabilityCalculation',
        icon: <SportsBarIcon />,
        authorized: true,
        roles: [UserRoles.ADMIN, UserRoles.TENANT_OWNER, UserRoles.TENANT_ASSIGNED]
    },
    {
        name: 'settings',
        path: '/settings',
        icon: <SettingsRoundedIcon />,
        authorized: true,
        roles: [UserRoles.ADMIN]
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