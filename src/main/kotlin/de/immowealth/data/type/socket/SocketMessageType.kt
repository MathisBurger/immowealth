package de.immowealth.data.type.socket

import kotlinx.serialization.Serializable

/**
 * The type of the socket message
 */
@Serializable
enum class SocketMessageType {
    /**
     * Message to keep alive the socket connection
     */
    KEEP_ALIVE,
    /**
     * A new message has been sent
     */
    NEW_MESSAGE
}