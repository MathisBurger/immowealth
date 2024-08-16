package de.immowealth.data.input

import jakarta.json.bind.annotation.JsonbCreator

/**
 * User input for registering a new user
 */
data class UserInput @JsonbCreator constructor(
    /**
     * Username of the user
     */
    val username: String,
    /**
     * Password of the user
     */
    val password: String,
    /**
     * Email of the user
     */
    val email: String,
    /**
     * Roles of the user
     */
    val roles: MutableSet<String>,
    /**
     * Tenant ID of the user
     */
    val tenantId: Long?,
)