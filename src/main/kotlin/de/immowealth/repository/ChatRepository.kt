package de.immowealth.repository

import de.immowealth.entity.Chat
import de.immowealth.entity.User
import jakarta.enterprise.context.ApplicationScoped

/**
 * The repository that handles chats
 */
@ApplicationScoped
class ChatRepository : AbstractRepository<Chat>() {

    /**
     * Finds all chats of an user
     *
     * @param user The user thats chats should be fetched
     */
    fun findByUser(user: User): List<Chat> {
        val cb = this.entityManager.criteriaBuilder;
        val cq = cb.createQuery(Chat::class.java)
        val root = cq.from(Chat::class.java)
        val partJoin = root.join<Chat, User>("participants")
        val condition = cb.equal(partJoin.get<Long>("id"),  user.id!!)
        cq.select(root).where(condition)
        return this.entityManager.createQuery(cq).resultList;
    }
}