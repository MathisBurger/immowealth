package de.mathisburger.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass

/**
 * Base entity that contains all field present in all entities
 */
@MappedSuperclass
abstract class BaseEntity {

    /**
     * The ID
     */
    @GeneratedValue
    @Id
    open var id: Long? = null;
}