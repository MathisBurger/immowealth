package de.immowealth.repository

import de.immowealth.entity.Chat
import de.immowealth.entity.ChatMessage
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.criteria.Root
import java.util.Date

@ApplicationScoped
class ChatMessageRepository : AbstractRepository<ChatMessage>() {

    /**
     * Finds all chat messages by chat ID and within message limit
     *
     * @param chatId The ID of the chat
     * @param limit The limit of messages
     * @param maxId The max ID of the latest message
     */
    fun findChatMessagesByChatIdAndLimit(chatId: Long, limit: Int, maxId: Long?): List<ChatMessage> {
        val cb = this.entityManager.criteriaBuilder;
        val cq = cb.createQuery(ChatMessage::class.java)
        val root: Root<ChatMessage> = cq.from(ChatMessage::class.java)
        val chatJoin = root.join<ChatMessage, Chat>("chat");
        val chatIdCondition = cb.equal(chatJoin.get<Long>("id"), chatId)
        val maxIdCondition = cb.lt(root.get<Long>("id"), maxId ?: -1)
        var finalCondition = chatIdCondition
        if (maxId != null) {
            finalCondition = cb.and(
                chatIdCondition,
                maxIdCondition
            )
        }
        cq.select(root).where(finalCondition).orderBy(cb.desc(root.get<Date>("createdAt")));
        val query = this.entityManager.createQuery(cq);
        query.maxResults = limit
        return query.resultList
    }
}