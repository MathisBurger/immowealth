package de.immowealth.startup

import de.immowealth.data.type.socket.SocketMessage
import de.immowealth.data.type.socket.SocketMessageType
import de.immowealth.socket.ChatSocket
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
 * Handler to keep all socket connections alive
 */
@ApplicationScoped
class ChatSocketKeepAlive {

    @Inject
    lateinit var chatSocket: ChatSocket;

    @Startup
    fun run() {
        val runnable: Runnable = object : Runnable {
            override fun run() {
                chatSocket.broadcast(SocketMessage(SocketMessageType.KEEP_ALIVE, ""), null);
            }
        };
        val thread = Thread(runnable)
        thread.start()
    }
}