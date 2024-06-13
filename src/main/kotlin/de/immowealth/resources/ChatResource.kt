package de.immowealth.resources

import de.immowealth.data.response.ChatResponse
import de.immowealth.entity.Chat
import de.immowealth.entity.ChatMessage
import de.immowealth.exception.ParameterException
import de.immowealth.service.ChatService
import graphql.GraphQLException
import io.quarkus.security.UnauthorizedException
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

/**
 * GraphQL resource for chats
 */
@GraphQLApi
class ChatResource {

    @Inject
    lateinit var chatService: ChatService

    /**
     * Gets all user chats
     */
    @Query
    fun getUserChats(): List<ChatResponse> {
        try {
            return this.chatService.getUserChats()
        } catch (e: ParameterException) {
            throw GraphQLException(e)
        }
    }

    /**
     * Gets chat messages
     */
    @Query
    fun getChatMessages(chatId: Long, limit: Int, maxId: Long?): List<ChatMessage> {
        try {
            return this.chatService.getChatMessages(chatId, limit, maxId)
        } catch (e: ParameterException) {
            throw GraphQLException(e)
        } catch (e: UnauthorizedException) {
            throw GraphQLException(e)
        }
    }

    /**
     * Creates a chat
     */
    @Mutation
    fun createChatWithUser(userId: Long): Chat {
        try {
            return this.chatService.createChatWithUser(userId);
        } catch (ex: ParameterException) {
            throw GraphQLException(ex)
        } catch (ex: UnauthorizedException) {
            throw GraphQLException(ex)
        }
    }

    /**
     * Sends a message to a chat
     */
    @Mutation
    fun sendMessage(chatId: Long, message: String): ChatMessage {
        try {
            return this.chatService.sendMessage(chatId, message);
        } catch (ex: ParameterException) {
            throw GraphQLException(ex)
        } catch (ex: UnauthorizedException) {
            throw GraphQLException(ex)
        }
    }
}