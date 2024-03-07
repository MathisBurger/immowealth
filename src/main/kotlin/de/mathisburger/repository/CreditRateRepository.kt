package de.mathisburger.repository

import de.mathisburger.entity.CreditRate
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

/**
 * Credit rate repository
 */
@ApplicationScoped
class CreditRateRepository : PanacheRepository<CreditRate> {
}