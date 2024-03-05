package de.mathisburger.data.input

import io.smallrye.graphql.api.AdaptToScalar
import io.smallrye.graphql.api.Scalar
import jakarta.json.bind.annotation.JsonbCreator
import java.util.*

data class UpdateRealEstateInput @JsonbCreator constructor(
    val id: Long,
    val city: String?,
    val zip: String?,
    val streetAndHouseNr: String?,
    val initialValue: Long?,
    @AdaptToScalar(Scalar.Date::class)
    val dateBought: Date?
)