package de.immowealth.data.input

import jakarta.json.bind.annotation.JsonbCreator

/**
 * Input to create a tenant
 */
data class CreateTenantInput @JsonbCreator constructor(
    /**
     * The name of the tenant
     */
    val name: String,
    /**
     * The username of the user
     */
    val username: String,
    /**
     * The password of the user
     */
    val password: String,
    /**
     * The email of the user
     */
    val email: String
)
