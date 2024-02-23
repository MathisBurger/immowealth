package de.mathisburger.data.input

import jakarta.json.bind.annotation.JsonbCreator

data class RealEstateInput @JsonbCreator constructor(
    val city: String,
    val zip: String,
    val streetAndHouseNr: String,
    val initialValue: Long
)
