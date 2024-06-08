package de.immowealth.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany

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
}