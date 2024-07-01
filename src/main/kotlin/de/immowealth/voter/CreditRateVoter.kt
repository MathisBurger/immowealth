package de.immowealth.voter

import de.immowealth.entity.Archived
import de.immowealth.entity.CreditRate
import de.immowealth.entity.User
import de.immowealth.entity.UserRoles
import de.immowealth.service.SecurityService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
 * Handles credit rate voting
 */
@ApplicationScoped
class CreditRateVoter : VoterInterface {

    companion object {
        val CREATE = "CREATE";
        val UPDATE = "UPDATE";
        val DELETE = "DELETE";
        val READ = "READ";
    }

    override fun <T : Archived> voteOnAttribute(user: User?, attributeName: String, value: T): Boolean {
        if (user == null) {
            return false;
        }
        if (user.roles.contains(UserRoles.ADMIN)) {
            return true;
        }
        if (value is CreditRate) {
            if (attributeName == CREATE || attributeName == UPDATE || attributeName == DELETE || attributeName == READ) {
                return value.tenant?.id == user.tenant?.id;
            }
        }
        return false
    }

    override fun votedType(): String {
        return CreditRate::class.java.toString()
    }
}