package de.immowealth.data.type

/**
 * The socket message that is sent by the socket
 */
data class SocketMessage(
    /**
     * The reason of the message
     */
    val reason: SocketMessageType,
    /**
     * The message of the message
     */
    val message: String
)
