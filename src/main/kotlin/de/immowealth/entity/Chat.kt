package de.immowealth.entity

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

/**
 * Chat entity
 */
@Entity
class Chat : BaseEntity() {

    /**
     * All participants of the chat
     */
    @ManyToMany(cascade = [(CascadeType.ALL)])
    @OnDelete(action = OnDeleteAction.CASCADE)
    var participants: MutableList<User> = mutableListOf()

    /**
     * All messages of the chat
     */
    @OneToMany(mappedBy = "chat", cascade = [CascadeType.ALL])
    var messages: MutableList<ChatMessage> = mutableListOf()

    /**
     * The real estate object assigned to the chat
     */
    @OneToOne
    var realEstateObject: RealEstateObject? = null

    /**
     * Whether the chat is a renter chat
     */
    var isRenterChat: Boolean? = false
}