package de.immowealth.data.input

import jakarta.json.bind.annotation.JsonbCreator
import java.util.Date

/**
 * The input to create a renter
 */
data class CreateRenterInput @JsonbCreator constructor(
    /**
     * The ID of the object
     */
    val objectID: Long,
    /**
     * The renters first name
     */
    val firstName: String,
    /**
     * The renters last name
     */
    val lastName: String,
    /**
     * The birthday of the renter
     */
    val birthDay: Date?,
)
