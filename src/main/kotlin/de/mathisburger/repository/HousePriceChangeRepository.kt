package de.mathisburger.repository

import de.mathisburger.entity.HousePriceChange
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class HousePriceChangeRepository : PanacheRepository<HousePriceChange> {

    fun findByZip(zip: String): List<HousePriceChange> {
        return find("zip", zip).list();
    }
}