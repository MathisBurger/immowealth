package de.immowealth.voter

import de.immowealth.entity.*
import jakarta.enterprise.context.ApplicationScoped

/**
 * Handles file voting
 */
@ApplicationScoped
class FileVoter : VoterInterface {

    companion object {
        val READ = "READ"
        val CREATE = "CREATE"
        val DELETE = "DELETE"
    }

    override fun <T : Archived> voteOnAttribute(user: User?, attributeName: String, value: T): Boolean {
        if (user == null) {
            return false;
        }
        if (user.roles.contains(UserRoles.ADMIN)) {
            return true;
        }
        if (value is UploadedFile) {
            if (attributeName == READ || attributeName == CREATE || attributeName == DELETE) {
                return user.tenant?.id == value.tenant?.id;
            }
        }
        return false
    }

    override fun votedType(): String {
        return UploadedFile::class.java.toString()
    }
}