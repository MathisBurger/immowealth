package de.immowealth.voter

import de.immowealth.entity.*
import jakarta.enterprise.context.ApplicationScoped

/**
 * Votes on settings
 */
@ApplicationScoped
class SettingsVoter : VoterInterface {

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
        if (value is Setting) {
            if (attributeName == READ || attributeName == UPDATE || attributeName == CREATE || attributeName == DELETE) {
                return user.id == value.user?.id;
            }
        }
        return false
    }

    override fun votedType(): String {
        return Setting::class.java.toString()
    }
}