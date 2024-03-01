package de.mathisburger.data.response

import de.mathisburger.entity.RealEstateObject
import jakarta.json.bind.annotation.JsonbCreator

data class ObjectResponse @JsonbCreator constructor(
    val realEstate: RealEstateObject,
    val creditRateSum: Double
)
