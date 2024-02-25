package de.mathisburger.data.type

import jakarta.json.bind.annotation.JsonbCreator

data class WealthSpreadType @JsonbCreator constructor(
    val id: Long,
    val value: Long,
    val label: String
)
