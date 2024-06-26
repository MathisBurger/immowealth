package de.immowealth.data.type.socket

import kotlinx.serialization.Serializable


/**
 * Notification that is sent over socket for a new message
 */
@Serializable
data class NewMessageNotification(
    /**
     * The ID of the chat
     */
    val chatId: Long,
    /**
     * The message that has been sent
     */
    val message: String,
)
