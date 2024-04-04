package de.mathisburger.repository

import de.mathisburger.entity.BaseEntity
import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.panache.common.Parameters

/**
 * Abstract repository that can be used to use archival feature internally
 */
abstract class AbstractRepository<T: BaseEntity> : PanacheRepository<T> {

    /**
     * Find with built-in archived filter in query.
     */
    fun findArchived(query: String, vararg params: Any, archived: Boolean = true): PanacheQuery<T> {
        if (archived) {
            if (query.isEmpty()) {
                return super.find("archived = ?1", true);
            }
            return super.find("archived = ?1 AND $query", true, params);
        }
        return super.find(query, params);
    }

    fun findAllArchived(): List<T> {
        return find("archived = ?1", true).list<T>();
    }
}