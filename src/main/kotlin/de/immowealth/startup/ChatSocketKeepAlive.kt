package de.immowealth.startup

import de.immowealth.data.type.socket.NewMessageNotification
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
        val runnable: Runnable = Runnable {
            while (true) {
                chatSocket.broadcast(SocketMessage(SocketMessageType.KEEP_ALIVE, NewMessageNotification(-1, "", -1, -1, -1)), null);
                Thread.sleep(10 * 1000);
            }
        };
        val thread = Thread(runnable)
        thread.start()
    }
}