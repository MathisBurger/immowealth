package de.immowealth.voter

import de.immowealth.entity.Archived
import de.immowealth.entity.Credit
import de.immowealth.entity.User
import de.immowealth.entity.UserRoles
import jakarta.enterprise.context.ApplicationScoped

/**
 * Handles votes on credit objects
 */
@ApplicationScoped
class CreditVoter : VoterInterface {

    companion object {
        val READ = "READ"
        val CREATE = "CREATE"
        val UPDATE = "UPDATE"
        val DELETE = "DELETE"
    }

    override fun <T : Archived> voteOnAttribute(user: User?, attributeName: String, value: T): Boolean {
        if (user == null) {
            return false;
        }
        if (user.roles.contains(UserRoles.ADMIN)) {
            return true;
        }
        if (value is Credit) {
            if (attributeName == READ || attributeName == UPDATE) {
                return user.tenant?.id == value.tenant?.id;
            }
            if (attributeName == CREATE || attributeName == DELETE) {
                return user.tenant?.id == value.tenant?.id &&
                        user.roles.contains(UserRoles.TENANT_OWNER);
            }
        }
        return false
    }

    override fun votedType(): String {
        return Credit::class.java.toString();
    }
}