package de.immowealth.data.request

import jakarta.json.bind.annotation.JsonbCreator

/**
 * Request for set passwords of user
 */
data class SetPasswordRequest @JsonbCreator constructor(
    /**
     * The new password of the user
     */
    val password: String,
    /**
     * Other value to make JSON parse work
     * NOTE: This value has no real purpose
     */
    val other: String
)