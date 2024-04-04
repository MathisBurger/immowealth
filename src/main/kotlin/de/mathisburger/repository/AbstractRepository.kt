package de.mathisburger.repository

import de.mathisburger.entity.BaseEntity
import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import java.util.*

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
}