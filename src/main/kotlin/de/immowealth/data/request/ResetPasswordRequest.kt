package de.immowealth.data.request

import jakarta.json.bind.annotation.JsonbCreator

/**
 * Request for resetting passwords of user
 */
data class ResetPasswordRequest @JsonbCreator constructor(
    /**
     * The username of the user
     */
    val username: String,
    /**
     * Other value to make JSON parse work
     * NOTE: This value has no real purpose
     */
    val other: String
)