package de.immowealth.service

import de.immowealth.entity.User
import de.immowealth.mock.MockSecurityContext
import de.immowealth.repository.UserRepository
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.SecurityContext

/**
 * Abstract test case utils
 */
abstract class AbstractServiceTest {

    /**
     * The user security context
     */
    abstract var securityContext: SecurityContext;


    /**
     * Logs in a user by user
     */
    fun loginAsUser(user: User) {
        if (securityContext is MockSecurityContext) {
            (this.securityContext as MockSecurityContext).principalName = user.username;
        }
    }

    /**
     * Logs in a user by username
     */
    fun loginAsUser(username: String) {
        if (securityContext is MockSecurityContext) {
            (this.securityContext as MockSecurityContext).principalName = username;
        }
    }

    /**
     * Logs out the user
     */
    fun logout() {
        if (securityContext is MockSecurityContext) {
            (this.securityContext as MockSecurityContext).principalName = null;
        }
    }

    /**
     * Removes all user except admin user
     */
    @Transactional
    fun removeAllUsersExceptAdmin(userRepository: UserRepository, entityManager: EntityManager) {
        val users = userRepository.listAll();
        for (user in users) {
            if (user.username != "admin") {
                entityManager.remove(user)
                entityManager.flush();

            }
        }
    }

}