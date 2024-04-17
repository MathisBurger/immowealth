package de.immowealth.data.input

import de.immowealth.entity.enum.AutoPayInterval
import io.smallrye.graphql.api.AdaptToScalar
import io.smallrye.graphql.api.Scalar
import jakarta.json.bind.annotation.JsonbCreator
import java.util.*

/**
 * Auto pay config input
 */
data class ConfigureAutoPayInput @JsonbCreator constructor(
    val id: Long,
    val enabled: Boolean,
    val interval: AutoPayInterval?,
    val amount: Double?,
    @AdaptToScalar(Scalar.Date::class)
    val startDate: Date
)
