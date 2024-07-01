package de.immowealth.voter

import de.immowealth.entity.Archived
import de.immowealth.entity.ConfigPreset
import de.immowealth.entity.User
import jakarta.enterprise.context.ApplicationScoped

/**
 * Voter that handles config preset access
 */
@ApplicationScoped
class ConfigPresetVoter : VoterInterface {

    companion object {
        final val READ = "READ";
        final val CREATE = "CREATE"
        final val UPDATE = "UPDATE";
    }

    override fun <T : Archived> voteOnAttribute(user: User?, attributeName: String, value: T): Boolean {
        if (user == null) {
            return false;
        }
        if (value is ConfigPreset) {
            if (attributeName == READ || attributeName == UPDATE || attributeName == CREATE) {
                return value.user?.id == user.id;
            }
        }
        return false;
    }

    override fun votedType(): String {
        return ConfigPreset::class.java.toString()
    }
}