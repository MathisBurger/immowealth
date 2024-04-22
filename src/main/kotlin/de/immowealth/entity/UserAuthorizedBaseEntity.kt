package de.immowealth.entity

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

/**
 * The authorized base entity that maps user authorized entities
 */
@MappedSuperclass
abstract class UserAuthorizedBaseEntity : Archived {

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
     * The user the entity is assigned to
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    open var user: User? = null;
}