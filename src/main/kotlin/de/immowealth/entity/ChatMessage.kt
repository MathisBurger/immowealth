package de.immowealth.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.ColumnDefault
import java.util.*

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

    /**
     * When the message has been created
     */
    var createdAt: Date? = null

    /**
     * Whether a message has been read or not
     */
    @ColumnDefault("false")
    var read: Boolean = false
}