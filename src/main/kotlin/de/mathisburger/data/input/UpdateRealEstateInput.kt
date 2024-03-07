package de.mathisburger.data.input

import io.smallrye.graphql.api.AdaptToScalar
import io.smallrye.graphql.api.Scalar
import jakarta.json.bind.annotation.JsonbCreator
import java.util.*

/**
 * Input to update real estate
 */
data class UpdateRealEstateInput @JsonbCreator constructor(
    /**
     * The ID of the real estate
     */
    val id: Long,
    /**
     * City of the real estate
     */
    val city: String?,
    /**
     * Zip of the real estate
     */
    val zip: String?,
    /**
     * Street and house nr of the real estate
     */
    val streetAndHouseNr: String?,
    /**
     * Initial value of the house
     */
    val initialValue: Long?,
    /**
     * Date the real estate was bought
     */
    @AdaptToScalar(Scalar.Date::class)
    val dateBought: Date?
)