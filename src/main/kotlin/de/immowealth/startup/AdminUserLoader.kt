package de.immowealth.startup

import de.immowealth.entity.User
import de.immowealth.repository.UserRepository
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.wildfly.security.password.PasswordFactory
import org.wildfly.security.password.interfaces.BCryptPassword

/**
 * Initializes the admin user
 */
@ApplicationScoped
class AdminUserLoader {

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    @Startup
    @Transactional
    fun run() {
        val user = this.userRepository.findByUserName("admin");
        if (user.isEmpty) {
            val admin = User()
            admin.username = "admin"
            admin.password = BcryptUtil.bcryptHash("admin123")
            admin.roles = mutableListOf("ADMIN");
            this.entityManager.persist(admin)
            this.entityManager.flush()
        }
    }
}