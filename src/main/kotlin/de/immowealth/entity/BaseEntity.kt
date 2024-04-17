package de.immowealth.entity

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.ColumnDefault

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

    /**
     * If the entity is archived
     */
    @Column(name = "archived")
    @ColumnDefault(value = "false")
    open var archived: Boolean = false;
}