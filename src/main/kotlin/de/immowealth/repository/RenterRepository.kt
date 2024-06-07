package de.immowealth.repository

import de.immowealth.entity.Renter
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RenterRepository : AbstractRepository<Renter>() {
}