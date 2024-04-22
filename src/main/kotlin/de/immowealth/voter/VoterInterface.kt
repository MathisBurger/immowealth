package de.immowealth.voter

import de.immowealth.entity.Archived
import de.immowealth.entity.User
import jakarta.persistence.EntityManager

/**
 * Handles voter actions
 */
interface VoterInterface {

    /**
     * Votes on a specific attribute
     *
     * @param user The user that should be voted on
     * @param attributeName The attribute like a role or so
     * @param value The value that should be voted on
     */
    fun <T : Archived> voteOnAttribute(user: User, attributeName: String, value: T): Boolean;

    /**
     * Gets the type that is voted on
     */
    fun votedType(): String;
}