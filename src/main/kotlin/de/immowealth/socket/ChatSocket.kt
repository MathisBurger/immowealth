package de.immowealth.socket

import de.immowealth.data.type.socket.SocketMessage
import de.immowealth.data.type.socket.SocketSession
import de.immowealth.entity.User
import de.immowealth.service.SecurityService
import io.smallrye.common.annotation.Blocking
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.control.ActivateRequestContext
import jakarta.inject.Inject
import jakarta.websocket.OnClose
import jakarta.websocket.OnOpen
import jakarta.websocket.Session
import jakarta.websocket.server.PathParam
import jakarta.websocket.server.ServerEndpoint
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.util.concurrent.ExecutorService

/**
 * The chat socket that handles live updates for chats
 */
@ApplicationScoped
@ServerEndpoint("/api/chat/{jwt}")
class ChatSocket {

    @Inject
    lateinit var securityService: SecurityService;

    @Inject
    lateinit var executor: ExecutorService;

    /**
     * All sessions that are currently connected
     */
    val sessions = mutableMapOf<Long, SocketSession>();

    @OnOpen
    fun onOpen(session: Session, @PathParam("jwt") jwt: String) {
        val id = this.securityService.getCurrentUserIdByToken(jwt);
        if (id !== null) {
            sessions[id] = SocketSession(id, session)
        } else {
            session.close();
        }
    }

    @OnClose
    fun onClose(session: Session, @PathParam("jwt") jwt: String) {
        val id = this.securityService.getCurrentUserIdByToken(jwt);
        if (id === null) {
            session.close();
        }
        if (this.sessions.containsKey(id)) {
            this.sessions.remove(id);
        }
    }

    /**
     * Broadcasts a message to a specific group of users
     *
     * @param message The message that should be sent
     * @param users The users that should receive the message
     */
    @ActivateRequestContext
    fun broadcast(message: SocketMessage, users: List<User>?) {
        var usersToBeUsed: MutableList<Long>? = null;
        if (users === null) {
            usersToBeUsed = sessions.keys.toMutableList()
        } else {
            usersToBeUsed = users.map { it.id!! }.toMutableList()
        }
        for (user in usersToBeUsed) {
            val session = this.sessions[user];
            if (session !== null) {
                val json = Json.encodeToString(message)
                session.session.asyncRemote.sendText(json);
            }
        }
    }
}