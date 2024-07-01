package de.immowealth.repository

import de.immowealth.entity.*
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Query
import jakarta.persistence.criteria.Join
import jakarta.persistence.criteria.Predicate

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
        val realEstateJoin = root.join<Chat, Tenant>("realEstateObject");
        var tenantCondition: Predicate? = null;
        if (user.tenant === null) {
            tenantCondition = cb.isNull(realEstateJoin.get<Tenant?>("tenant"));
        } else {
            tenantCondition = cb.equal(realEstateJoin.get<Tenant?>("tenant"), user.tenant);
        }
        var renterCondition: Predicate? = null;
        if (user.renter !== null) {
            val renters = realEstateJoin.get<List<Renter>>("renters");
            renterCondition = cb.isMember(user.renter!!, renters);
        }

        var finalMidCondition: Predicate? = null;
        if (renterCondition !== null) {
            finalMidCondition = cb.or(tenantCondition, renterCondition)
        } else {
            finalMidCondition = tenantCondition;
        }
        val finalCondition = cb.and(
            cb.equal(root.get<Boolean>("isRenterChat"), true),
            finalMidCondition,
            cb.not(inClause)
        );
        cq.select(root).where(finalCondition);
        return this.entityManager.createQuery(cq).resultList;
    }

}