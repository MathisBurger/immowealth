package de.immowealth.entity

import de.immowealth.graphql.PasswordAdapter
import de.immowealth.persistance.StringListConverter
import io.smallrye.graphql.api.AdaptWith
import jakarta.persistence.*
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.Type

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
    @Convert(converter = StringListConverter::class)
    @Column(name = "roles", columnDefinition = "text")
    var roles: MutableSet<String> = mutableSetOf();

    /**
     * The tenant the user is assigned to
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    var tenant: Tenant? = null;

    /**
     * All chats the user is member of
     */
    @ManyToMany(cascade = [jakarta.persistence.CascadeType.ALL])
    var chats: MutableList<Chat> = mutableListOf();

    /**
     * The messages that were sent by the user
     */
    @OneToMany
    var messages: MutableList<ChatMessage> = mutableListOf();

    /**
     * The renter assigned to the user
     */
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    var renter: Renter? = null;

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