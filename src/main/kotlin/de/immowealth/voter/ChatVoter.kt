package de.immowealth.voter

import de.immowealth.entity.Archived
import de.immowealth.entity.Chat
import de.immowealth.entity.User
import de.immowealth.entity.UserRoles
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ChatVoter : VoterInterface {

    companion object {
        val CREATE = "CREATE"
        val READ = "READ"
        val SEND_MESSAGE = "SEND_MESSAGE"
    }

    override fun <T : Archived> voteOnAttribute(user: User?, attributeName: String, value: T): Boolean {
        if (user == null) {
            return false
        }
        if (value is Chat) {
            return (value.participants.contains(user)
                    || (value.isRenterChat == true && value.realEstateObject?.tenant?.id === user.tenant?.id && (
                    user.roles.contains(UserRoles.ADMIN)
                            || user.roles.contains(UserRoles.TENANT_ASSIGNED)
                            || user.roles.contains(UserRoles.TENANT_OWNER)
                    )
                    )) || (value.isRenterChat == true && value.realEstateObject?.renters?.mapNotNull { it.user?.id }
                ?.contains(user.id) == true)
        }
        return false;
    }

    override fun votedType(): String {
        return Chat::class.java.toString();
    }
}