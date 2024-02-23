package de.mathisburger.data.input

import jakarta.json.bind.annotation.JsonbCreator

data class CreditInput @JsonbCreator constructor(
    val amount: Long,
    val interestRate: Double,
    val redemptionRate: Double,
    val bank: String
)
