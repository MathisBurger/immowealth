package de.immowealth.repository

import de.immowealth.entity.*
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
        val partCondition = cb.equal(partJoin.get<Long>("id"),  user.id!!);
        cq.select(root).where(partCondition);
        return this.entityManager.createQuery(cq).resultList;
    }

    fun getRenterChats(user: User, ignore: List<Long>): List<Chat> {
        val cb = this.entityManager.criteriaBuilder;
        val cq = cb.createQuery(Chat::class.java)
        val root = cq.from(Chat::class.java);
        val inClause = cb.`in`(root.get<Long>("id"));
        for (ignored in ignore) {
            inClause.value(ignored);
        }
        val finalCondition = cb.and(
            cb.equal(root.get<Boolean>("isRenterChat"), true),
            cb.not(inClause)
        );
        cq.select(root).where(finalCondition);
        return this.entityManager.createQuery(cq).resultList;
    }

}