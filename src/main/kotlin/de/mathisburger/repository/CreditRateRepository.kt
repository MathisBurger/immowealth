package de.mathisburger.repository

import de.mathisburger.entity.CreditRate
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CreditRateRepository : PanacheRepository<CreditRate> {
}