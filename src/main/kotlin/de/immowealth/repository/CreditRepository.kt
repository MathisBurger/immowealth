package de.immowealth.repository

import de.immowealth.entity.Credit
import jakarta.enterprise.context.ApplicationScoped
import java.util.Calendar

/**
 * Credit repository
 */
@ApplicationScoped
class CreditRepository : AbstractRepository<Credit>() {

    /**
     * Finds all that are enabled and required for auto booking
     *
     * @return All enabled elements
     */
    fun findAllWithAutoBookingRequired(): List<Credit> {
        val today = Calendar.getInstance().time;
        return find("archived IS FALSE AND autoPayInterval IS NOT NULL AND nextCreditRate <= ?1", today).list();
    }
}