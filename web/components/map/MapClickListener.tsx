import {GetMapObjectsDocument, MapObjectFragment, useUpdateRealEstateMutation} from "@/generated/graphql";
import {useMapEvents} from "react-leaflet";
import {ChangeElement} from "@/app/map/mapPageComponent";

interface MapClickListenerProps {
    data: ChangeElement;
    onFinish: () => void;
}

const MapClickListener = ({data, onFinish}: MapClickListenerProps) => {

    const [mutation] = useUpdateRealEstateMutation({
        refetchQueries: [
            {
                query: GetMapObjectsDocument
            }
        ]
    });


    const map = useMapEvents({
        click(e) {
            if (data.clientY !== e.originalEvent.clientY || data.clientX !== e.originalEvent.clientX) {
                mutation({
                    variables: {
                        input: {
                            id: data.element.id,
                            positionLat: e.latlng.lat,
                            positionLon: e.latlng.lng
                        }
                    }
                }).then((res) => {
                    if (res.errors === undefined) {
                        onFinish();
                    }
                })
            }
        }
    });

    return (
        <div></div>
    );
}

export default MapClickListener;