package de.mathisburger.repository

import de.mathisburger.entity.RealEstateObject
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RealEstateRepository : PanacheRepository<RealEstateObject> {
}