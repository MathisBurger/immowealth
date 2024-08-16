package de.immowealth.entity

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.ColumnDefault

/**
 * The authorized base entity that maps tenant authorized entities
 */
@MappedSuperclass
abstract class AuthorizedBaseEntity : Archived {

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

    /**
     * The tenant the entity is assigned to
     */
    @ManyToOne
    open var tenant: Tenant? = null;
}