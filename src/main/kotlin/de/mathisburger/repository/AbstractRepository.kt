package de.mathisburger.repository

import de.mathisburger.entity.BaseEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository

/**
 * Abstract repository that can be used to use archival feature internally
 */
abstract class AbstractRepository<T: BaseEntity> : PanacheRepository<T> {
}