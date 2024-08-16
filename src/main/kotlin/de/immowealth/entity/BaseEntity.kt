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
abstract class BaseEntity : Archived {

    /**
     * The ID
     */
    @GeneratedValue
    @Id
    override var id: Long? = null;

    /**
     * If the entity is archived
     */
    @Column(name = "archived")
    @ColumnDefault(value = "false")
    override var archived: Boolean = false;
}