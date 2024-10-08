package de.immowealth.voter

import de.immowealth.entity.Archived
import de.immowealth.entity.AuthorizedBaseEntity
import de.immowealth.entity.User
import de.immowealth.entity.UserRoles
import jakarta.enterprise.context.ApplicationScoped

/**
 * Handles user votes
 */
@ApplicationScoped
class UserVoter : VoterInterface {

    companion object {
        final val CREATE = "CREATE"
        final val UPDATE = "UPDATE"
        final val DELETE = "DELETE"
        final val READ = "READ"
    }

    override fun <T : Archived> voteOnAttribute(user: User?, attributeName: String, value: T): Boolean {
        if (user == null) {
            return false
        }
        println(user.roles.toList())
        if (user.roles.contains(UserRoles.ADMIN)) {
            return true;
        }
        if (value is User) {
            if (attributeName == CREATE) {
                return user.roles.contains(UserRoles.ADMIN)
                        || (user.tenant?.id === value.tenant?.id && user.roles.contains(UserRoles.TENANT_OWNER) && user.tenant?.id != null && !value.roles.contains(UserRoles.ADMIN));
            }
            if (attributeName == READ) {
                return user.roles.contains(UserRoles.TENANT_OWNER) && user.tenant?.id === value.tenant?.id;
            }
        }
        return false
    }

    override fun votedType(): String {
        return User::class.java.toString();
    }
}