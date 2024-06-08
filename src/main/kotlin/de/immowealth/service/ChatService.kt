package de.immowealth.service

import de.immowealth.entity.Chat
import de.immowealth.exception.ParameterException
import de.immowealth.repository.ChatRepository
import de.immowealth.repository.UserRepository
import de.immowealth.voter.ChatVoter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional

/**
 * The service handling chats
 */
@ApplicationScoped
class ChatService : AbstractService() {

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    lateinit var chatRepository: ChatRepository;

    /**
     * Creates a chat with another user.
     *
     * @param userId The ID of the other user
     */
    @Transactional
    fun createChatWithUser(userId: Long): Chat {
        val otherUser = this.userRepository.findByIdOptional(userId)
        if (otherUser.isEmpty) {
            throw ParameterException("Invalid user");
        }
        val currentUser = this.securityService.getCurrentUser();
        if (currentUser === null) {
            throw ParameterException("Current user not present in security");
        }
        val chat = Chat();
        chat.participants.add(currentUser);
        chat.participants.add(otherUser.get());
        this.denyUnlessGranted(ChatVoter.CREATE, chat);
        this.entityManager.persist(chat);
        currentUser.chats.add(chat);
        otherUser.get().chats.add(chat);
        this.entityManager.persist(currentUser);
        this.entityManager.persist(otherUser.get());
        this.entityManager.flush();
        return chat;
    }
}