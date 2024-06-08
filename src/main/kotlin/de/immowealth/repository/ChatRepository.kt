package de.immowealth.repository

import de.immowealth.entity.Chat
import jakarta.enterprise.context.ApplicationScoped

/**
 * The repository that handles chats
 */
@ApplicationScoped
class ChatRepository : AbstractRepository<Chat>() {
}