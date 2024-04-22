package de.immowealth.entity

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

/**
 * The authorized base entity that maps user authorized entities
 */
@MappedSuperclass
abstract class UserAuthorizedBaseEntity {

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

    /**
     * The user the entity is assigned to
     */
    @ManyToOne
    open var user: User? = null;
}