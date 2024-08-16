package de.immowealth.voter

import de.immowealth.entity.*
import jakarta.enterprise.context.ApplicationScoped

/**
 * Votes on object rent expenses
 */
@ApplicationScoped
class ObjectRentExpenseVoter : VoterInterface {

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
        if (value is ObjectRentExpense) {
            if (attributeName == READ || attributeName == UPDATE || attributeName == CREATE || attributeName == DELETE) {
                return user.tenant?.id == value.tenant?.id;
            }
        }
        return false
    }

    override fun votedType(): String {
        return ObjectRentExpense::class.java.toString()
    }
}