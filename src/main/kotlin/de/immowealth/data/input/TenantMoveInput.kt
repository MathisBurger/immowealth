package de.immowealth.data.input

import jakarta.json.bind.annotation.JsonbCreator

/**
 * The input to move users between tenants
 */
data class TenantMoveInput @JsonbCreator constructor(
    /**
     * The ID of the tenant
     */
    val id: Long,
    /**
     * All user IDs that should be assigned to tenant
     */
    val users: MutableList<Long>
)
