package de.immowealth.entity

/**
 * Implemented on entities that can be marked as favourite
 */
interface Favourite {

    /**
     * Checks if entity is favourite of person
     */
    fun isFavourite(user: User?): Boolean
}