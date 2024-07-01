package de.immowealth.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne

/**
 * Chat entity
 */
@Entity
class Chat : BaseEntity() {

    /**
     * All participants of the chat
     */
    @ManyToMany
    var participants: MutableList<User> = mutableListOf()

    /**
     * All messages of the chat
     */
    @OneToMany(mappedBy = "chat")
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