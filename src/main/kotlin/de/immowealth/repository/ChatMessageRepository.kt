package de.immowealth.repository

import de.immowealth.entity.ChatMessage
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ChatMessageRepository : AbstractRepository<ChatMessage>() {
}