package de.immowealth.repository

import de.immowealth.entity.ChatMessage
import de.immowealth.entity.RealEstateObject
import de.immowealth.entity.Renter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.criteria.Root

@ApplicationScoped
class RenterRepository : AbstractRepository<Renter>() {


    /**
     * Finds all unassigned renters
     */
    fun findUnassigned(): List<Renter> {
        val cb =  this.entityManager.criteriaBuilder;
        val cq = cb.createQuery(Renter::class.java);
        val root: Root<Renter> = cq.from(Renter::class.java)
        cq.select(root).where(cb.isNull(root.get<RealEstateObject>("realEstateObject")));
        val query = this.entityManager.createQuery(cq);
        return query.resultList;
    }
}