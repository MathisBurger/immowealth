package de.immowealth.repository

import de.immowealth.entity.BaseEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository

/**
 * Abstract repository that can be used to use archival feature internally
 */
abstract class AbstractRepository<T: BaseEntity> : PanacheRepository<T> {

    /**
     * Finds all unarchived entries.
     */
    fun findAllArchived(): List<T> {
        return find("archived IS FALSE").list<T>();
    }

    /**
     * Finds all archived entities
     */
    fun findAllThatAreArchived(): List<T> {
        return find("archived IS TRUE").list<T>();
    }
}