package de.immowealth.data.input

import jakarta.json.bind.annotation.JsonbCreator

/**
 * The credit input for creating credits
 */
data class CreditInput @JsonbCreator constructor(
    /**
     * The credit amount
     */
    val amount: Long,
    /**
     * The interest rate
     */
    val interestRate: Double,
    /**
     * The redemption rate
     */
    val redemptionRate: Double,
    /**
     * The credit bank
     */
    val bank: String
)
