package de.immowealth.data.response

import de.immowealth.entity.Credit
import jakarta.json.bind.annotation.JsonbCreator

/**
 * The credit response
 */
data class CreditResponse @JsonbCreator constructor(
    /**
     * The credit
     */
    val credit: Credit,
    /**
     * The sum of all credit rates
     */
    val creditRateSum: Double,
    /**
     * The credit rate accumulation steps
     */
    val creditRateCummulationSteps: List<Double>,
    /**
     * Linked real estate object id
     */
    val realEstateObjectId: Long,
    /**
     * Archived state
     */
    val archived: Boolean
);
