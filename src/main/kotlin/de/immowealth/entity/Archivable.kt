package de.immowealth.entity

/**
 * Defines an archivable entity
 */
interface Archivable {

    /**
     * Converts entity to string
     */
    override fun toString(): String;

    /**
     * Gets the name of the entity
     */
    fun getEntityName(): String
}