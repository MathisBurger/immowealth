package de.immowealth.voter

import de.immowealth.entity.Archived
import de.immowealth.entity.ChatMessage
import de.immowealth.entity.User
import jakarta.enterprise.context.ApplicationScoped

/**
 * The voter to vote on chat messages
 */
@ApplicationScoped
class ChatMessageVoter : VoterInterface {

    companion object {
        val CREATE = "CREATE"
    }

    override fun <T : Archived> voteOnAttribute(user: User?, attributeName: String, value: T): Boolean {
        if (user == null) {
            return false
        }
        if (value is ChatMessage) {
            return value.sender?.id === user.id;
        }
        return false
    }

    override fun votedType(): String {
        return ChatMessage::class.java.toString()
    }
}