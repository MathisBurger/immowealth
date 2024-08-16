package de.immowealth.repository

import de.immowealth.entity.Credit
import de.immowealth.entity.RealEstateObject
import jakarta.enterprise.context.ApplicationScoped

/**
 * Real estate repository
 */
@ApplicationScoped
class RealEstateRepository : AbstractRepository<RealEstateObject>() {

    /**
     * Gets a real estate object by credit
     */
    fun getByCredit(credit: Credit): RealEstateObject {
        return find("from RealEstateObject o where o.credit.id = ?1", credit.id).firstResult()
    }

    /**
     * Finds an object by city
     */
    fun findByCity(city: String): RealEstateObject {
        return find("from RealEstateObject o where o.city = ?1", city).firstResult();
    }
}