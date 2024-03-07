'use client';
import {Box, Breadcrumbs, Link, Typography} from "@mui/joy";
import ChevronRightRoundedIcon from '@mui/icons-material/ChevronRightRounded';
import HomeRoundedIcon from '@mui/icons-material/HomeRounded';
import {useCallback, useEffect, useMemo, useState} from "react";
import routes from "@/routeConfig";
import {RouteConfigType} from "@/typings/routeConfig";
import {usePathname} from "next/navigation";

/**
 * Displays the path of the current location
 * @constructor
 */
const PathDisplay = () => {

    const pathname = usePathname();

    const findRoute = useCallback((path: string, pos: RouteConfigType[]): RouteConfigType|null => {
        for (let p of pos) {
            if (p.path === path) {
                return p;
            }
            if (p.children) {
                let childRes = findRoute(path, p.children);
                if (childRes !== null) {
                    return childRes;
                }
            }
        }
        return null;
    }, []);

    const selectedRoute = useMemo<RouteConfigType[]>(() => {
        let results = [];
        let spl = pathname.split('/');
        for (let i=1; i<=spl.length; i++) {
            let res = findRoute(spl.slice(0, i).join('/'), routes);
            if (res !== null) {
                results.push(res);
            }
        }
        return results;
    },
    [routes, pathname, findRoute]);

    const route = useMemo<RouteConfigType|null>(
        () => findRoute(pathname, routes),
        [pathname, findRoute]
    );

    const lastId = useMemo<string>(() => {
        let spl = pathname.split('/');
        return spl[spl.length-1];
    }, [pathname]);


    return (
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <Breadcrumbs
                size="sm"
                aria-label="breadcrumbs"
                separator={<ChevronRightRoundedIcon fontSize="small" />}
                sx={{ pl: 0 }}
            >
                <Link
                    underline="none"
                    color="neutral"
                    href="/"
                    aria-label="Home"
                >
                    <HomeRoundedIcon />
                </Link>
                {selectedRoute.map((part) => (
                    <Link
                        key={part.path}
                        underline="hover"
                        color="neutral"
                        href={part.path}
                        fontSize={12}
                        fontWeight={500}
                    >
                        {part.name}
                    </Link>
                ))}
                {route === null ? (
                    <Typography color="primary" fontWeight={500} fontSize={12}>
                        {lastId}
                    </Typography>
                ) : null}
            </Breadcrumbs>
        </Box>
    )
}

export default PathDisplay;