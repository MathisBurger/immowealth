'use client';
import {
    Avatar,
    Box, Divider,
    GlobalStyles,
    IconButton,
    List,
    ListItem,
    ListItemButton,
    listItemButtonClasses,
    ListItemContent,
    Sheet, SvgIcon, Typography
} from "@mui/joy";
import {closeSidebar} from "@/utilts/sidebarUtils";
import LogoutRoundedIcon from '@mui/icons-material/LogoutRounded';
import routes from "@/routeConfig";
import {useRouter} from "next/navigation";
import {useTranslation} from "next-export-i18n";
import useCurrentUser from "@/hooks/useCurrentUser";
import {useMemo} from "react";
import {RouteConfigType} from "@/typings/routeConfig";
import {isUniqueArray} from "@/utilts/arrayUtils";

/**
 * The sidebar
 *
 * @constructor
 */
const Sidebar = () => {

    const router = useRouter();
    const {t} = useTranslation();
    const currentUser = useCurrentUser();

    const filteredRoutes = useMemo<RouteConfigType[]>(
        () => routes.filter((r) => r.authorized ? currentUser !== null : true)
            .filter((r) => r.roles ? !isUniqueArray([...r.roles, ...(currentUser?.roles ?? [])]) : true),
        [router, currentUser]
    );

    const logout = () => {
        document.cookie = "";
        router.push("/login");
    }

    return (
        <Sheet
            className="Sidebar"
            sx={{
                position: { xs: 'fixed', md: 'sticky' },
                transform: {
                    xs: 'translateX(calc(100% * (var(--SideNavigation-slideIn, 0) - 1)))',
                    md: 'none',
                },
                transition: 'transform 0.4s, width 0.4s',
                zIndex: 10000,
                height: '100dvh',
                width: 'var(--Sidebar-width)',
                top: 0,
                p: 2,
                flexShrink: 0,
                display: 'flex',
                flexDirection: 'column',
                gap: 2,
                borderRight: '1px solid',
                borderColor: 'divider',
            }}
        >
            <GlobalStyles
                styles={(theme) => ({
                    ':root': {
                        '--Sidebar-width': '220px',
                        [theme.breakpoints.up('lg')]: {
                            '--Sidebar-width': '240px',
                        },
                    },
                })}
            />
            <Box
                className="Sidebar-overlay"
                sx={{
                    position: 'fixed',
                    zIndex: 9998,
                    top: 0,
                    left: 0,
                    width: '100vw',
                    height: '100vh',
                    opacity: 'var(--SideNavigation-slideIn)',
                    backgroundColor: 'var(--joy-palette-background-backdrop)',
                    transition: 'opacity 0.4s',
                    transform: {
                        xs: 'translateX(calc(100% * (var(--SideNavigation-slideIn, 0) - 1) + var(--SideNavigation-slideIn, 0) * var(--Sidebar-width, 0px)))',
                        lg: 'translateX(-100%)',
                    },
                }}
                onClick={() => closeSidebar()}
            />
            <Box
                sx={{
                    minHeight: 0,
                    overflow: 'hidden auto',
                    flexGrow: 1,
                    display: 'flex',
                    flexDirection: 'column',
                    [`& .${listItemButtonClasses.root}`]: {
                        gap: 1.5,
                    },
                }}
            >
                <List
                    size="sm"
                    sx={{
                        gap: 1,
                        '--List-nestedInsetStart': '30px',
                        '--ListItem-radius': (theme) => theme.vars.radius.sm,
                    }}
                >
                    {filteredRoutes.map((route) => (
                        <ListItem key={route.path}>
                            <ListItemButton onClick={() => router.push(route.path)}>
                                {route.icon ? route.icon : null}
                                <ListItemContent>
                                    <Typography level="title-sm">{t(`routes.${route.name}`)}</Typography>
                                </ListItemContent>
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
            </Box>
            <Divider />
            {currentUser && (
                <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
                    <Avatar
                        variant="outlined"
                        size="sm"
                        src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=286"
                    />
                    <Box sx={{ minWidth: 0, flex: 1 }}>
                        <Typography level="title-sm">{currentUser.username}</Typography>
                        <Typography level="body-xs">{currentUser.email}</Typography>
                    </Box>
                    <IconButton size="sm" variant="plain" color="neutral" onClick={logout}>
                        <LogoutRoundedIcon />
                    </IconButton>
                </Box>
            )}
        </Sheet>
    );
}

export default Sidebar;