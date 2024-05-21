package de.immowealth.service

import de.immowealth.entity.Tenant
import de.immowealth.entity.User
import de.immowealth.entity.UserRoles
import de.immowealth.repository.TenantRepository
import de.immowealth.repository.UserRepository
import de.immowealth.voter.TenantVoter
import graphql.GraphQLException
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

    @Inject
    lateinit var userRepository: UserRepository;

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

    /**
     * Gets all tenants
     */
    fun getAllTenants(): List<Tenant> {
        return this.filterAccess(TenantVoter.READ, this.tenantRepository.listAll());
    }

    /**
     * Gets a tenant by ID
     *
     * @param id The ID of the tenant
     */
    fun getTenant(id: Long): Tenant {
        val ten = this.tenantRepository.findByIdOptional(id);
        if (ten.isEmpty) {
            throw  GraphQLException("Tenant not found");
        }
        val tenant = ten.get();
        this.denyUnlessGranted(TenantVoter.READ, tenant);
        return tenant;
    }

    /**
     * Moves users between tenants.
     *
     * @param id The ID of the tenant
     * @param users All users that should now be members
     * @return The updated tenant
     */
    @Transactional
    fun moveUsersBetweenTenant(id: Long, users: MutableList<Long>): Tenant {
        val ten = this.tenantRepository.findByIdOptional(id);
        if (ten.isEmpty) {
            throw  GraphQLException("Tenant not found");
        }
        val tenant = ten.get();
        this.denyUnlessGranted(TenantVoter.SWITCH_USERS, tenant);
        for (user in tenant.users) {
            user.tenant = null;
            this.entityManager.persist(user);
        }
        val newUsers: MutableList<User> = users
            .map { this.userRepository.findByIdOptional(it) }
            .filter { it.isPresent }
            .map { it.get() }.toMutableList();
        tenant.users = newUsers;
        this.entityManager.persist(tenant);
        for (user in newUsers) {
            user.tenant = tenant;
            this.entityManager.persist(user);
        }
        this.entityManager.flush();
        return tenant;
    }
}