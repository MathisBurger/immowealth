package de.immowealth.data.type.socket

import de.immowealth.entity.User
import jakarta.websocket.Session

/**
 * The socket session for chat socket
 */
data class SocketSession(
    /**
     * The user that is connected to the session
     */
    val userId: Long,
    /**
     * The actual session
     */
    val session: Session
)
