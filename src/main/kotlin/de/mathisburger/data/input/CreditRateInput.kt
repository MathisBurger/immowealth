package de.mathisburger.data.input

import io.smallrye.graphql.api.AdaptToScalar
import io.smallrye.graphql.api.Scalar
import jakarta.json.bind.annotation.JsonbCreator
import java.util.Date

data class CreditRateInput @JsonbCreator constructor(
    val id: Long,
    val rate: Double,
    @AdaptToScalar(Scalar.Date::class)
    val date: Date
)
