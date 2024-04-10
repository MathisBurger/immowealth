package de.immowealth.data.type

import jakarta.json.bind.annotation.JsonbCreator

/**
 * Type of wealth spread
 */
data class WealthSpreadType @JsonbCreator constructor(
    /**
     * Object ID
     */
    val id: Long,
    /**
     * The value of the asset
     */
    val value: Long,
    /**
     * The label of the asset
     */
    val label: String
)
