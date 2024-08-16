package de.immowealth.data.type.socket

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * The socket message that is sent by the socket
 */
@Serializable
data class SocketMessage(
    /**
     * The reason of the message
     */
    val reason: SocketMessageType,
    /**
     * The message of the message
     */
    val message: NewMessageNotification
);
