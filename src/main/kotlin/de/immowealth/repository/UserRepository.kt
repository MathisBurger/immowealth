package de.immowealth.repository

import de.immowealth.entity.User
import jakarta.enterprise.context.ApplicationScoped
import java.util.Optional

/**
 * Handling user transactions
 */
@ApplicationScoped
class UserRepository : AbstractRepository<User>() {

    /**
     * Finds a user by username
     *
     * @param username The username of the user
     */
    fun findByUserName(username: String): Optional<User> {
        return find("username", username).firstResultOptional();
    }
}