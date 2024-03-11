package de.mathisburger.data.input

import jakarta.json.bind.annotation.JsonbCreator

/**
 * Update house prices input
 */
data class UpdateHousePriceChangeInput @JsonbCreator constructor(
    /**
     * The ID of the entity
     */
    val id: Long,
    /**
     * The zip
     */
    val zip: String?,
    /**
     * The year
     */
    val year: Int?,
    /**
     * The change
     */
    val change: Double?,
)
