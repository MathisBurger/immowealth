package de.mathisburger.data.response

import de.mathisburger.data.type.WealthSpreadType
import jakarta.json.bind.annotation.JsonbCreator

/**
 * The wealth response for displaying data in the dashboard
 */
data class WealthResponse @JsonbCreator constructor(
    /**
     * Total wealth
     */
    val total: Long,
    /**
     * Detailed positions
     */
    val detailed: List<WealthSpreadType>
)
