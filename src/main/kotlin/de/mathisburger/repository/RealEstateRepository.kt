package de.mathisburger.repository

import de.mathisburger.entity.RealEstateObject
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

/**
 * Real estate repository
 */
@ApplicationScoped
class RealEstateRepository : PanacheRepository<RealEstateObject> {
}