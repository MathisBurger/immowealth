package de.mathisburger.data.response

import jakarta.json.bind.annotation.JsonbCreator

/**
 * Value of a real estate at a specific year
 */
data class PriceValueRelation @JsonbCreator constructor(
    /**
     * The current value of the real estate
     */
    val value: Long,
    /**
     * The year
     */
    val year: Int
);
