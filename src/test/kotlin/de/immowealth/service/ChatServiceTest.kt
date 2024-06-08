package de.immowealth.service

import de.immowealth.entity.User
import de.immowealth.exception.ParameterException
import de.immowealth.repository.UserRepository
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

@QuarkusTest
class ChatServiceTest : AbstractServiceTest() {

    @Inject
    override lateinit var securityContext: JsonWebToken

    @Inject
    lateinit var entityManager: EntityManager

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var chatService: ChatService

    @Test
    @Order(1)
    fun testCreateChatAsUser() {
        this.createUsers();
        val admin = this.userRepository.findByUserName("admin").get();
        this.loginAsUser("chat_user");
        assertDoesNotThrow { this.chatService.createChatWithUser(admin.id!!); }
    }

    @Test
    @Order(2)
    fun testCreateChatAsNonUser() {
        val admin = this.userRepository.findByUserName("admin").get();
        this.logout();
        assertThrows(ParameterException::class.java) { this.chatService.createChatWithUser(admin.id!!);}
    }

    @Test
    @Order(3)
    fun testCreateChatWithInvalidUser() {
        this.loginAsUser("admin")
        assertThrows(ParameterException::class.java) { this.chatService.createChatWithUser(-1);}
    }

    @Transactional
    fun createUsers() {
        val user = User()
        user.username = "chat_user"
        user.email = ""
        user.password = ""
        user.roles = mutableListOf();
        this.entityManager.persist(user)
        this.entityManager.flush();
    }
}