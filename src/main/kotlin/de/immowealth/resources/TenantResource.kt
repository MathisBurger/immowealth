package de.immowealth.resources

import de.immowealth.data.input.CreateTenantInput
import de.immowealth.entity.Tenant
import de.immowealth.service.TenantService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

/**
 * Handles all tenant queries and mutations
 */
@GraphQLApi
class TenantResource {

    @Inject
    lateinit var tenantService: TenantService;

    /**
     * Gets all tenants
     */
    @Query
    fun getAllTenants(): List<Tenant> {
        return tenantService.getAllTenants();
    }

    /**
     * Gets a tenant by ID
     */
    @Query
    fun getTenant(id: Long): Tenant {
        return this.tenantService.getTenant(id);
    }

    /**
     * Creates a new tenant
     */
    @Mutation
    fun createTenant(input: CreateTenantInput): Tenant {
        return tenantService.createTenant(
            input.name,
            input.username,
            input.password,
            input.email
        );
    }
}