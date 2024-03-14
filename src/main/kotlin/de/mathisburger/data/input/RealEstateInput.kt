package de.mathisburger.data.input


import io.smallrye.graphql.api.AdaptToScalar
import io.smallrye.graphql.api.Scalar
import jakarta.json.bind.annotation.JsonbCreator
import java.util.Date

/**
 * The real estate input for creating real estate
 */
data class RealEstateInput @JsonbCreator constructor(
    /**
     * City of the real estate
     */
    val city: String,
    /**
     * Zip of the real estate
     */
    val zip: String,
    /**
     * Street and house nr of the real estate
     */
    val streetAndHouseNr: String,
    /**
     * Initial value of the house
     */
    val initialValue: Long,
    /**
     * The credit that exists alongside with house
     */
    val credit: CreditInput,
    /**
     * Date the real estate was bought
     */
    @AdaptToScalar(Scalar.Date::class)
    val dateBought: Date,
    /**
     * Amount of rooms
     */
    val rooms: Double,
    /**
     * Space for living
     */
    val space: Double,
    /**
     * Type of the object
     * e.g. house or flat (1. OG, EG)
     */
    val objectType: String,
    /**
     * Construction year
     */
    val constructionYear: Int,
    /**
     * Renovation year
     */
    val renovationYear: Int?,
    /**
     * Energy efficiency class
     */
    val energyEfficiency: String?,
    /**
     * Gross return of the object investment
     */
    val grossReturn: Double?,
    /**
     * Garden exists
     */
    val garden: Boolean,
    /**
     * Kitchen exists
     */
    val kitchen: Boolean,
    /**
     * Heating type (wood, oil, gas)
     */
    val heatingType: String,
    /**
     * Notes
     */
    val notes: String?
)
