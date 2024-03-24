package de.mathisburger.data.api

import jakarta.json.bind.annotation.JsonbCreator

/**
 * Simple exchange response of the foreign exchange rate API
 */
data class ExchangeResponse @JsonbCreator constructor(
    /**
     * All API exchange rates
     */
    val fixingRates: List<ApiExchangeRate> = mutableListOf()
)
