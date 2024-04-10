package de.immowealth.data.input

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
    val dateBought: Date?,
    /**
     * Position lat
     */
    val positionLat: Double?,
    /**
     * Position lon
     */
    val positionLon: Double?,
    /**
     * Amount of rooms
     */
    val rooms: Double?,
    /**
     * Space for living
     */
    val space: Double?,
    /**
     * Type of the object
     * e.g. house or flat (1. OG, EG)
     */
    val objectType: String?,
    /**
     * Construction year
     */
    val constructionYear: Int?,
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
    val garden: Boolean?,
    /**
     * Kitchen exists
     */
    val kitchen: Boolean?,
    /**
     * Heating type (wood, oil, gas)
     */
    val heatingType: String?,
    /**
     * General notes
     */
    val notes: String?
)