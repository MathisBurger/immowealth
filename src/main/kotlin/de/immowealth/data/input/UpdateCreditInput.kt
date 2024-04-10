package de.immowealth.data.input

import jakarta.json.bind.annotation.JsonbCreator

/**
 * The update credit input for creating credits
 */
data class UpdateCreditInput @JsonbCreator constructor(
    /**
     * The ID of the credit that should be updated
     */
    var id: Long,
    /**
     * The credit amount
     */
    val amount: Long?,
    /**
     * The interest rate
     */
    val interestRate: Double?,
    /**
     * The redemption rate
     */
    val redemptionRate: Double?,
    /**
     * The credit bank
     */
    val bank: String?
)