package de.immowealth.data.request

import jakarta.json.bind.annotation.JsonbCreator

/**
 * Login request for logging in users
 */
data class LoginRequest @JsonbCreator constructor(
    /**
     * The username of the user
     */
    val username: String,
    /**
     * The password of the user
     */
    val password: String
)
