package de.mathisburger.repository

import de.mathisburger.entity.HousePriceChange
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

/**
 * House price change repository
 */
@ApplicationScoped
class HousePriceChangeRepository : PanacheRepository<HousePriceChange> {

    /**
     * Finds all with specific zip
     *
     * @param zip The zip that should be searched for
     * @return All changes with zip
     */
    fun findByZip(zip: String): List<HousePriceChange> {
        return find("zip", zip).list();
    }
}