package de.mathisburger.data.api

import jakarta.json.bind.annotation.JsonbCreator

/**
 * API exchange rate response model
 */
data class ApiExchangeRate @JsonbCreator constructor(
    /**
     * The symbol of the currency
     */
    val currency: String,
    /**
     * The offer rate of the currency
     */
    val offerRate: String,
)
