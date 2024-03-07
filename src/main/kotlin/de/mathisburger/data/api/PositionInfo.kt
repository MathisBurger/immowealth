package de.mathisburger.data.api

import jakarta.json.bind.annotation.JsonbCreator

/**
 * The position info that is related to all positioned objects
 */
data class PositionInfo @JsonbCreator constructor(
    /**
     * Lat value
     */
    val lat: String,
    /**
     * Long value
     */
    val lon: String
)