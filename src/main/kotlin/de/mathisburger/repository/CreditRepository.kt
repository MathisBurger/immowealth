package de.mathisburger.repository

import de.mathisburger.entity.Credit
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CreditRepository : PanacheRepository<Credit> {
}