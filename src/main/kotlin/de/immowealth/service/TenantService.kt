package de.immowealth.service

import de.immowealth.entity.Tenant
import de.immowealth.entity.UserRoles
import de.immowealth.repository.TenantRepository
import de.immowealth.voter.TenantVoter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional

/**
 * The service handling tenant transactions
 */
@ApplicationScoped
class TenantService : AbstractService() {

    @Inject
    lateinit var tenantRepository: TenantRepository;

    @Inject
    lateinit var userService: UserService;

    /**
     * Creates a new tenant
     *
     * @param name The name of the tenant
     * @param username The username of the owner
     * @param password The password of the owner
     * @param email The email of the owner
     */
    @Transactional
    fun createTenant(name: String, username: String, password: String, email: String): Tenant {
        val tenant = Tenant()
        this.denyUnlessGranted(TenantVoter.CREATE, tenant);
        tenant.name = name;
        this.entityManager.persist(tenant);
        this.entityManager.flush();
        this.userService.registerUser(username, password, email, mutableListOf(UserRoles.TENANT_OWNER), tenant.id);
        return tenant;
    }
}