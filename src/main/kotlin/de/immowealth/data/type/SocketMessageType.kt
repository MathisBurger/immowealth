package de.immowealth.data.type

/**
 * The type of the socket message
 */
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