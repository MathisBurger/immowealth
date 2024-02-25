package de.mathisburger.data.response

import de.mathisburger.data.type.WealthSpreadType
import jakarta.json.bind.annotation.JsonbCreator

data class WealthResponse @JsonbCreator constructor(
    val total: Long,
    val detailed: List<WealthSpreadType>
)
