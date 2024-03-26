package de.mathisburger.repository

import de.mathisburger.entity.Credit
import de.mathisburger.entity.RealEstateObject
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.Optional

/**
 * Real estate repository
 */
@ApplicationScoped
class RealEstateRepository : PanacheRepository<RealEstateObject> {

    /**
     * Gets a real estate object by credit
     */
    fun getByCredit(credit: Credit): RealEstateObject {
        return find("from RealEstateObject o where o.credit.id = ?1", credit.id).firstResult()
    }
}