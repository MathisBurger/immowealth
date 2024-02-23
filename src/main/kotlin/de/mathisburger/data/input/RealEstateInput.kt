package de.mathisburger.data.input


import io.smallrye.graphql.api.AdaptToScalar
import io.smallrye.graphql.api.Scalar
import jakarta.json.bind.annotation.JsonbCreator
import java.util.Date

data class RealEstateInput @JsonbCreator constructor(
    val city: String,
    val zip: String,
    val streetAndHouseNr: String,
    val initialValue: Long,
    val credit: CreditInput,
    @AdaptToScalar(Scalar.Date::class)
    val dateBought: Date
)
