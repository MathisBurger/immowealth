package de.immowealth.service

import de.immowealth.entity.User
import de.immowealth.exception.ParameterException
import de.immowealth.exception.UserExistsException
import de.immowealth.repository.TenantRepository
import de.immowealth.repository.UserRepository
import de.immowealth.startup.SettingsLoader
import de.immowealth.voter.UserVoter
import io.quarkus.elytron.security.common.BcryptUtil
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional

/**
 * Handles all user transactions
 */
@ApplicationScoped
class UserService : AbstractService() {

    @Inject
    lateinit var settingsLoader: SettingsLoader;

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    lateinit var tenantRepository: TenantRepository

    /**
     * Registers a user on tenant or not
     *
     * @param username The username of the user
     * @param password The password of the user
     * @param email The email of the user
     * @param roles All roles of the user
     * @param tenantId The ID of the tenant the user should be assigned to
     */
    @Transactional
    fun registerUser(username: String, password: String, email: String, roles: MutableSet<String>, tenantId: Long? = null): User {
        val exists = this.userRepository.findByUserName(username).isPresent;
        if (exists) {
            throw UserExistsException("User already registered");
        }
        val user = User()
        user.username = username;
        user.password = BcryptUtil.bcryptHash(password)
        user.email = email;
        user.roles = roles;
        if (tenantId == null) {
            this.denyUnlessGranted(UserVoter.CREATE, user)
            this.entityManager.persist(user);
        } else {
            val tenant = this.tenantRepository.findByIdOptional(tenantId);
            if (tenant.isEmpty) {
                throw ParameterException("Tenant not found");
            }
            tenant.get().users.add(user);
            user.tenant = tenant.get();
            this.denyUnlessGranted(UserVoter.CREATE, user)
            this.entityManager.persist(user);
            this.entityManager.persist(tenant.get());
        }
        this.entityManager.flush();
        this.settingsLoader.initWithUser(user);
        return user;
    }

    /**
     * Gets the data of the current logged-in user
     */
    fun getCurrentUser(): User? {
        return this.securityService.getCurrentUser();
    }

    /**
     * Gets all users the user has access to
     */
    fun getAllUsers(): List<User> {
        return this.filterAccess(UserVoter.READ, this.userRepository.listAll());
    }
}