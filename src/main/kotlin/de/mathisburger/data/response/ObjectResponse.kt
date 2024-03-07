package de.mathisburger.data.response

import de.mathisburger.entity.RealEstateObject
import jakarta.json.bind.annotation.JsonbCreator

/**
 * The response of a real estate
 */
data class ObjectResponse @JsonbCreator constructor(
    /**
     * The real estate
     */
    val realEstate: RealEstateObject,
    /**
     * The sum of all paid credit rates
     */
    val creditRateSum: Double,
    /**
     * All credit rate accumulation steps
     */
    val creditRateCummulationSteps: List<Double>,
    /**
     * All price changes
     */
    val priceChanges: List<PriceValueRelation>,
    /**
     * The price forecast
     */
    val priceForecast: List<PriceValueRelation>,
    /**
     * The estimated market value
     */
    val estimatedMarketValue: Long
);
