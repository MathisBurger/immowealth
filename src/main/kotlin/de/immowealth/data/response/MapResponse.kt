package de.immowealth.data.response

import de.immowealth.entity.RealEstateObject
import jakarta.json.bind.annotation.JsonbCreator

/**
 * Map response
 */
data class MapResponse @JsonbCreator constructor(
    /**
     * All objects that should be shown on map
     */
    val objects: List<RealEstateObject>,
    /**
     * Middle lat of the map
     */
    val lat: Double,
    /**
     * Middle lon of the map
     */
    val lon: Double
)
