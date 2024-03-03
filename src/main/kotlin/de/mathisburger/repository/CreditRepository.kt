package de.mathisburger.repository

import de.mathisburger.entity.Credit
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.Calendar

@ApplicationScoped
class CreditRepository : PanacheRepository<Credit> {

    fun findAllWithAutoBookingRequired(): List<Credit> {
        val today = Calendar.getInstance().time;
        return find("autoPayInterval IS NOT NULL AND nextCreditRate <= ?1", today).list();
    }
}