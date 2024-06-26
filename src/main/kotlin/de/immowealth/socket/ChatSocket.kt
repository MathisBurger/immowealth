package de.immowealth.socket

import de.immowealth.data.type.socket.SocketMessage
import de.immowealth.data.type.socket.SocketSession
import de.immowealth.entity.User
import de.immowealth.service.SecurityService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.websocket.OnClose
import jakarta.websocket.OnOpen
import jakarta.websocket.Session
import jakarta.websocket.server.ServerEndpoint

/**
 * The chat socket that handles live updates for chats
 */
@ApplicationScoped
@ServerEndpoint("/api/chat")
class ChatSocket {

    @Inject
    lateinit var securityService: SecurityService;

    /**
     * All sessions that are currently connected
     */
    val sessions = mutableMapOf<String, SocketSession>();

    @OnOpen
    fun onOpen(session: Session) {
        val user = this.securityService.getCurrentUser();
        if (user === null) {
            session.close();
            return;
        }
        sessions.put(user.username, SocketSession(user, session))
    }

    @OnClose
    fun onClose(session: Session) {
        val user = this.securityService.getCurrentUser();
        if (user === null) {
            session.close();
        }
        this.sessions.remove(user!!.username);
    }

    /**
     * Broadcasts a message to a specific group of users
     *
     * @param message The message that should be sent
     * @param users The users that should receive the message
     */
    fun broadcast(message: SocketMessage, users: List<User>?) {
        var usersToBeUsed: MutableList<User>? = null;
        if (users === null) {
            usersToBeUsed = sessions.map { it.value.user }.toMutableList()
        } else {
            usersToBeUsed = users.toMutableList();
        }
        for (user in usersToBeUsed) {
            val session = this.sessions.get(user.username);
            if (session !== null) {
                session.session.asyncRemote.sendObject(message);
            }
        }
    }
}