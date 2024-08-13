package de.immowealth.startup

import de.immowealth.entity.User
import de.immowealth.repository.UserRepository
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

/**
 * Initializes the admin user
 */
@ApplicationScoped
class AdminUserLoader {

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    @Inject
    lateinit var settingsLoader: SettingsLoader

    @Startup
    @Transactional
    fun run() {
        val user = this.userRepository.findByUserName("admin");
        if (user.isEmpty) {
            val admin = User()
            admin.username = "admin"
            admin.password = BcryptUtil.bcryptHash("admin123")
            admin.roles = mutableSetOf("ROLE_ADMIN");
            this.entityManager.persist(admin)
            this.entityManager.flush();
            this.settingsLoader.initWithUser(admin);
        }
    }
}