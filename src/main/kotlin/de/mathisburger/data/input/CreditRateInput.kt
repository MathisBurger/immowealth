package de.mathisburger.data.input

import io.smallrye.graphql.api.AdaptToScalar
import io.smallrye.graphql.api.Scalar
import jakarta.json.bind.annotation.JsonbCreator
import java.util.Date

/**
 * Input of a credit rate
 */
data class CreditRateInput @JsonbCreator constructor(
    /**
     * ID of the credit
     */
    val id: Long,
    /**
     * The rate
     */
    val rate: Double,
    @AdaptToScalar(Scalar.Date::class)
    /**
     * The date of the booking
     */
    val date: Date
)
