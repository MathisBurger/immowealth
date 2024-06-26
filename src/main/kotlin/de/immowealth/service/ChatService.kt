package de.immowealth.service

import de.immowealth.data.response.ChatResponse
import de.immowealth.data.type.socket.NewMessageNotification
import de.immowealth.data.type.socket.SocketMessage
import de.immowealth.data.type.socket.SocketMessageType
import de.immowealth.entity.Chat
import de.immowealth.entity.ChatMessage
import de.immowealth.exception.ParameterException
import de.immowealth.repository.ChatMessageRepository
import de.immowealth.repository.ChatRepository
import de.immowealth.repository.UserRepository
import de.immowealth.socket.ChatSocket
import de.immowealth.voter.ChatMessageVoter
import de.immowealth.voter.ChatVoter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import java.util.*

/**
 * The service handling chats
 */
@ApplicationScoped
class ChatService : AbstractService() {

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    lateinit var chatRepository: ChatRepository;

    @Inject
    lateinit var chatMessageRepository: ChatMessageRepository;

    @Inject
    lateinit var chatSocket: ChatSocket;

    /**
     * Creates a chat with another user.
     *
     * @param userId The ID of the other user
     */
    @Transactional
    fun createChatWithUser(userId: Long): Chat {
        val otherUser = this.userRepository.findByIdOptional(userId)
        if (otherUser.isEmpty) {
            throw ParameterException("Invalid user");
        }
        val currentUser = this.securityService.getCurrentUser();
        if (currentUser === null) {
            throw ParameterException("Current user not present in security");
        }
        val chat = Chat();
        chat.participants.add(currentUser);
        chat.participants.add(otherUser.get());
        this.denyUnlessGranted(ChatVoter.CREATE, chat);
        this.entityManager.persist(chat);
        currentUser.chats.add(chat);
        otherUser.get().chats.add(chat);
        this.entityManager.persist(currentUser);
        this.entityManager.persist(otherUser.get());
        this.entityManager.flush();
        return chat;
    }

    /**
     * Sends a message to a chat
     *
     * @param chatId The ID of the chat
     * @param message The message to send
     */
    @Transactional
    fun sendMessage(chatId: Long, message: String): ChatMessage {
        val optionalChat = this.chatRepository.findByIdOptional(chatId)
        if (optionalChat.isEmpty) {
            throw ParameterException("Chat not found");
        }
        val chat = optionalChat.get();
        val msg = ChatMessage()
        val sender = this.securityService.getCurrentUser();
        msg.chat = chat;
        msg.message = message;
        msg.createdAt = Date();
        msg.sender = sender;
        this.denyUnlessGranted(ChatMessageVoter.CREATE, msg)
        this.entityManager.persist(msg);
        chat.messages.add(msg)
        this.denyUnlessGranted(ChatVoter.SEND_MESSAGE, chat)
        this.entityManager.persist(chat);
        this.entityManager.flush();
        this.chatSocket.broadcast(
            SocketMessage(SocketMessageType.NEW_MESSAGE, NewMessageNotification(
                chat.id!!,
                message
            )),
            chat.participants.filter { it.id !== sender?.id }
        );
        return msg;
    }

    /**
     * Gets all user chats
     */
    fun getUserChats(): List<ChatResponse> {
        val currentUser = this.securityService.getCurrentUser();
        if (currentUser == null) {
            throw ParameterException("Current user not present in security");
        }
        val chats = this.chatRepository.findByUser(currentUser);
        val responses = mutableListOf<ChatResponse>();
        for (chat in chats) {
            var count = 0;
            var index = chat.messages.lastIndex;
            while (index >= 0 && !chat.messages.get(index).read && chat.messages.get(index).sender!!.id !== currentUser.id) {
                count++;
                index--;
            }
            responses.add(ChatResponse(chat, count));
        }
        return responses;
    }

    /**
     * Gets chat messages in chat
     *
     * @param chatId The ID of the chat
     * @param limit The limit of messages
     * @param maxId The max id
     */
    fun getChatMessages(chatId: Long, limit: Int, maxId: Long?): List<ChatMessage> {
        val chat = this.chatRepository.findByIdOptional(chatId)
        if (chat.isEmpty) {
            throw ParameterException("Chat not found");
        }
        this.denyUnlessGranted(ChatVoter.READ, chat.get())
        return this.chatMessageRepository.findChatMessagesByChatIdAndLimit(chatId, limit, maxId).reversed()
    }

    /**
     * Reads all chat messages that were not sent by the user itself
     *
     * @param chatId The ID of the chat
     */
    @Transactional
    fun readChatMessages(chatId: Long) {
        val currentUser = this.securityService.getCurrentUser();
        if (currentUser == null) {
            throw ParameterException("Current user not present in security");
        }
        val chat = this.chatRepository.findByIdOptional(chatId)
        if (chat.isEmpty) {
            throw ParameterException("Chat not found");
        }
        this.denyUnlessGranted(ChatVoter.READ, chat.get())
        val realChat = chat.get();
        var index = realChat.messages.lastIndex;
        while (index >= 0 && !realChat.messages.get(index).read && realChat.messages.get(index).sender!!.id !== currentUser.id) {
            val msg = realChat.messages.get(index)
            msg.read = true;
            this.entityManager.persist(msg);
            index--;
        }
        if (realChat.messages.lastIndex != index) {
            this.entityManager.flush();
        }
    }
}