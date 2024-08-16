package de.immowealth.entity

/**
 * Implemented on entities that can be marked as favourite
 */
interface Favourite : Archivable {

    var favourite: MutableList<User>

    /**
     * Checks if entity is favourite of person
     */
    fun isFavourite(user: User?): Boolean
}