package de.immowealth.repository

import de.immowealth.entity.HousePriceChange
import jakarta.enterprise.context.ApplicationScoped

/**
 * House price change repository
 */
@ApplicationScoped
class HousePriceChangeRepository : AbstractRepository<HousePriceChange>() {

    /**
     * Finds all with specific zip
     *
     * @param zip The zip that should be searched for
     * @return All changes with zip
     */
    fun findByZip(zip: String): List<HousePriceChange> {
        return find("archived IS FALSE AND zip = ?1", zip).list();
    }
}