package de.immowealth.repository

import de.immowealth.entity.CreditRate
import jakarta.enterprise.context.ApplicationScoped

/**
 * Credit rate repository
 */
@ApplicationScoped
class CreditRateRepository : AbstractRepository<CreditRate>() {
}