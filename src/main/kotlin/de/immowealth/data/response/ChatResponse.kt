package de.immowealth.data.response

import com.fasterxml.jackson.annotation.JsonCreator
import de.immowealth.entity.Chat

/**
 * The response for a chat
 */
data class ChatResponse @JsonCreator constructor(
    /**
     * The chat entity
     */
    val chat: Chat,
    /**
     * The amount of unread messages
     */
    val unreadMessagesCount: Int
)