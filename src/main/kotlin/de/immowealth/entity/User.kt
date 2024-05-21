package de.immowealth.entity

import de.immowealth.graphql.PasswordAdapter
import io.smallrye.graphql.api.AdaptWith
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

/**
 * The user entity
 */
@Entity(name = "immowealth_user")
class User : BaseEntity(), Archivable {

    /**
     * The username of the user
     */
    @Column(columnDefinition = "TEXT")
    var username: String = "";

    /**
     * The password of the user
     */
    @AdaptWith(PasswordAdapter::class)
    var password: String = "";

    /**
     * The email of the user
     */
    var email: String = "";

    /**
     * All roles of the user
     */
    @ElementCollection(fetch = FetchType.EAGER)
    var roles: MutableList<String> = mutableListOf();

    /**
     * The tenant the user is assigned to
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    var tenant: Tenant? = null;

    override fun toString(): String {
        return this.username;
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String? {
        // TODO: Implement direct url to user
        return "";
    }
}