package de.immowealth.service

import de.immowealth.entity.Chat
import de.immowealth.exception.ParameterException
import de.immowealth.repository.ChatRepository
import de.immowealth.repository.UserRepository
import de.immowealth.voter.ChatVoter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
 * The service handling chats
 */
@ApplicationScoped
class ChatService : AbstractService() {

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    lateinit var chatRepository: ChatRepository;

    fun createChatWithUser(userId: Long): Chat {
        val otherUser = this.userRepository.findByIdOptional(userId)
        if (otherUser.isEmpty) {
            throw ParameterException("Invalid user");
        }
        val currentUser = this.securityService.getCurrentUser();
        if (currentUser === null) {
            throw ParameterException("Current user not present in security");
        }
        val chat = Chat()
        chat.participants.add(currentUser);
        currentUser.chats.add(chat);
        chat.participants.add(otherUser.get());
        otherUser.get().chats.add(chat);
        this.denyUnlessGranted(ChatVoter.CREATE, chat);
        this.entityManager.persist(chat);
        this.entityManager.persist(currentUser);
        this.entityManager.persist(otherUser.get());
        this.entityManager.flush();
        return chat;
    }
}