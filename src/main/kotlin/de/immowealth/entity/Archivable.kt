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
    fun getEntityName(): String;

    /**
     * Gets the direct url to entity
     */
    fun getDirectUrl(): String?;

    /**
     * The ID of the entity
     */
    var id: Long?;
}