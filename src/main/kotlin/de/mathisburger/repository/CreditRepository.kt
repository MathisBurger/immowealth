package de.mathisburger.repository

import de.mathisburger.entity.Credit
import io.quarkus.hibernate.orm.panache.PanacheRepository
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
        return find("autoPayInterval IS NOT NULL AND nextCreditRate <= ?1", today).list();
    }
}