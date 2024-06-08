package de.immowealth.voter

import de.immowealth.entity.Archived
import de.immowealth.entity.Chat
import de.immowealth.entity.User
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ChatVoter : VoterInterface {

    companion object {
        val CREATE = "CREATE"
    }

    override fun <T : Archived> voteOnAttribute(user: User?, attributeName: String, value: T): Boolean {
        if (user == null) {
            return false
        }
        if (value is Chat) {
            return value.participants.contains(user)
        }
        return false;
    }

    override fun votedType(): String {
        return Chat::class.java.toString();
    }
}