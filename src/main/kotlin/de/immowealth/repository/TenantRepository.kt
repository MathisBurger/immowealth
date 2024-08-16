package de.immowealth.repository

import de.immowealth.entity.Tenant
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class TenantRepository : AbstractRepository<Tenant>() {
}