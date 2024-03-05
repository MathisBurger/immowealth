package de.mathisburger.data.api

import jakarta.json.bind.annotation.JsonbCreator

data class PositionInfo @JsonbCreator constructor(
    val lat: String,
    val lon: String
)