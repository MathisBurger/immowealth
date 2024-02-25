'use client';
import {Box, Breadcrumbs, Link, Typography} from "@mui/joy";
import ChevronRightRoundedIcon from '@mui/icons-material/ChevronRightRounded';
import HomeRoundedIcon from '@mui/icons-material/HomeRounded';
import {useRouter} from "next/navigation";
import {useMemo} from "react";
import routes from "@/routeConfig";
import {RouteConfigType} from "@/typings/routeConfig";

const PathDisplay = () => {

    const router = useRouter();

    const findRoute = (path: string, pos: RouteConfigType[]): RouteConfigType|null => {
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
    }

    const selectedRoute = useMemo<RouteConfigType[]>(() => {
        let results = [];
        let spl = document.location.pathname.split('/');
        for (let i=1; i<=spl.length; i++) {
            let res = findRoute(spl.slice(0, i).join('/'), routes);
            if (res !== null) {
                results.push(res);
            }
        }
        return results;
    },
    [routes]);

    const route = useMemo<RouteConfigType|null>(
        () => findRoute(document.location.pathname, routes),
        [document.location.pathname]
    );

    const lastId = useMemo<string>(() => {
        let spl = document.location.pathname.split('/');
        return spl[spl.length-1];
    }, [document.location.pathname]);


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