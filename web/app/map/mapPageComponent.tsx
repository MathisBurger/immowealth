'use client';
import React, {useEffect, useRef, useState} from "react";
import {MapContainer, Marker, Popup, TileLayer, useMapEvents} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import {MapObjectFragment, useGetMapObjectsQuery} from "@/generated/graphql";
import {Alert, Button, CircularProgress, Grid, Typography} from "@mui/joy";
import {useRouter} from "next/navigation";
import MapClickListener from "@/components/map/MapClickListener";

export interface ChangeElement {
    element: MapObjectFragment;
    clientX: number;
    clientY: number;
}

const MapPage = () => {

    const {data} = useGetMapObjectsQuery();
    const mapRef = useRef(null);
    const router = useRouter();
    const latitude = 48.7760256;
    const longitude = 11.4456758;
    const [loaded, setLoaded] = useState<boolean>(false);
    const [changeElement, setChangeElement] = useState<ChangeElement|null>(null);


    useEffect(() => {
        setLoaded(true);
    }, []);

    if (loaded) {
        return (
            <>
                {changeElement && (
                    <Alert color="warning">
                        Du befindest dich im Korrektur-Modus.
                        <Button color="danger" onClick={() => setChangeElement(null)}>Abbrechen</Button>
                    </Alert>
                )}
                <MapContainer
                    center={[data?.mapObjects.lat ?? latitude, data?.mapObjects.lon ?? longitude]}
                    zoom={13}
                    ref={mapRef}
                    style={{height: "100%", width: "100%"}}
                >
                    <TileLayer
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    {changeElement && (
                        <MapClickListener data={changeElement} onFinish={() => setChangeElement(null)} />
                    )}
                    {changeElement === null && (
                        data?.mapObjects.objects.map((obj) => (
                            <Marker position={[obj?.positionLat ?? 0, obj?.positionLon ?? 0]} key={obj?.id}>
                                <Popup>
                                    <Typography level="h3">
                                        Objekt {obj?.id}
                                    </Typography>
                                    <Typography level="h4">
                                        {obj?.streetAndHouseNr}, {obj?.zip} {obj?.city}
                                    </Typography>
                                    <Grid container direction="row" spacing={2}>
                                        <Grid>
                                            <Button onClick={() => router.push(`/objects/details?id=${obj?.id}`)}>
                                                Details
                                            </Button>
                                        </Grid>
                                        <Grid>
                                            <Button onClick={(e) => setChangeElement({
                                                element: obj as MapObjectFragment,
                                                clientX: e.clientX,
                                                clientY: e.clientY
                                            })} color="neutral">
                                                Position korrigieren
                                            </Button>
                                        </Grid>
                                    </Grid>
                                </Popup>
                            </Marker>
                        ))
                    )}
                    {/* Additional map layers or components can be added here */}
                </MapContainer>
            </>
        );
    }
    return <CircularProgress variant="plain" />;

}

export default MapPage;