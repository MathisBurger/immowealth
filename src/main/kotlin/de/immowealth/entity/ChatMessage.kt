package de.immowealth.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne

/**
 * Chat message entity
 */
@Entity
class ChatMessage : BaseEntity() {

    /**
     * The chat the message is assigned to
     */
    @ManyToOne
    var chat: Chat? = null

    /**
     * The sender
     */
    @ManyToOne
    var sender: User? = null

    /**
     * The actual message of the message
     */
    @Column(columnDefinition = "TEXT")
    var message: String? = null
}