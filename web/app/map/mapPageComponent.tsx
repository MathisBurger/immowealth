'use client';
import React, {useEffect, useRef, useState} from "react";
import {MapContainer, Marker, Popup, TileLayer} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import {useGetMapObjectsQuery} from "@/generated/graphql";
import {Button, CircularProgress, Typography} from "@mui/joy";
import {useRouter} from "next/navigation";
import LoadingSpinner from "@/components/LoadingSpinner";

const MapPage = () => {

    const {data} = useGetMapObjectsQuery();
    const mapRef = useRef(null);
    const router = useRouter();
    const latitude = 48.7760256;
    const longitude = 11.4456758;
    const [loaded, setLoaded] = useState<boolean>(false);
    useEffect(() => {
        setLoaded(true);
    }, []);

    if (loaded) {
        return (
            <MapContainer center={[latitude, longitude]} zoom={13} ref={mapRef} style={{height: "100%", width: "100%"}}>
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                {data?.allObjects.map((obj) => (
                    <Marker position={[obj?.positionLat ?? 0, obj?.positionLon ?? 0]} key={obj?.id}>
                        <Popup>
                            <Typography level="h3">
                                Objekt {obj?.id}
                            </Typography>
                            <Typography level="h4">
                                {obj?.streetAndHouseNr}, {obj?.zip} {obj?.city}
                            </Typography>
                            <Button onClick={() => router.push(`/objects/details?id=${obj?.id}`)}>
                                Details
                            </Button>
                        </Popup>
                    </Marker>
                ))}
                {/* Additional map layers or components can be added here */}
            </MapContainer>
        );
    }
    return <CircularProgress variant="plain" />;

}

export default MapPage;