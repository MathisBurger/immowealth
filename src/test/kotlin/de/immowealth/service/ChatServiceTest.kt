package de.immowealth.service

import de.immowealth.entity.User
import de.immowealth.exception.ParameterException
import de.immowealth.repository.ChatRepository
import de.immowealth.repository.UserRepository
import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
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

    @Inject
    lateinit var chatRepository: ChatRepository

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

    @Test
    @Order(4)
    fun testSendMessageAsUser() {
        val admin = this.userRepository.findByUserName("admin").get();
        val chat = this.chatRepository.findByUser(admin).get(0);
        this.loginAsUser("chat_user");
        assertDoesNotThrow { this.chatService.sendMessage(chat.id!!, "Message"); }
    }

    @Test
    @Order(5)
    fun testSendMessageAsNonUser() {
        val admin = this.userRepository.findByUserName("admin").get();
        val chat = this.chatRepository.findByUser(admin).get(0);
        this.logout()
        assertThrows(UnauthorizedException::class.java) { this.chatService.sendMessage(chat.id!!, "msg");}
    }

    @Test
    @Order(6)
    fun testSendMessageWithInvalidChat() {
        this.loginAsUser("admin")
        assertThrows(ParameterException::class.java) { this.chatService.sendMessage(-1, "msg");}
    }

    @Test
    @Order(7)
    fun testGetChatMessagesAsUser() {
        val admin = this.userRepository.findByUserName("admin").get();
        val chat = this.chatRepository.findByUser(admin).get(0);
        this.loginAsUser("chat_user");
        assertDoesNotThrow { this.chatService.getChatMessages(chat.id!!, 100, null); }
    }

    @Test
    @Order(8)
    fun testGetChatMessagesAsNonUser() {
        val admin = this.userRepository.findByUserName("admin").get();
        val chat = this.chatRepository.findByUser(admin).get(0);
        this.logout()
        assertThrows(UnauthorizedException::class.java) { this.chatService.getChatMessages(chat.id!!, 100, null);}
    }

    @Test
    @Order(9)
    fun testGetChatMessagesWithInvalidChat() {
        this.loginAsUser("admin")
        assertThrows(ParameterException::class.java) { this.chatService.getChatMessages(-1, 100, null);}
    }

    @Test
    @Order(10)
    fun testGetChatsAsUser() {
        this.loginAsUser("chat_user");
        val res = this.chatService.getUserChats();
        assertTrue(res.isNotEmpty())
    }

    @Test
    @Order(11)
    fun testGetChatsAsNonUser() {
        this.logout()
        assertThrows(ParameterException::class.java) { this.chatService.getUserChats() }
    }

    @Test
    @Order(12)
    fun testReadMessagesAsChatUser() {
        val admin = this.userRepository.findByUserName("admin").get();
        val chat = this.chatRepository.findByUser(admin).get(0);
        this.loginAsUser("chat_user");
        assertDoesNotThrow { this.chatService.readChatMessages(chat.id!!) }
    }

    @Test
    @Order(13)
    fun testReadMessagesAsNonChatUser() {
        val admin = this.userRepository.findByUserName("admin").get();
        val chat = this.chatRepository.findByUser(admin).get(0);
        this.loginAsUser("admin2");
        assertThrows(ParameterException::class.java) { this.chatService.readChatMessages(chat.id!!) }
    }

    @Test
    @Order(14)
    fun testReadMessagesAsNonUser() {
        val admin = this.userRepository.findByUserName("admin").get();
        val chat = this.chatRepository.findByUser(admin).get(0);
        this.logout()
        assertThrows(ParameterException::class.java) { this.chatService.readChatMessages(chat.id!!) }
    }


    @Transactional
    fun createUsers() {
        val user = User()
        user.username = "chat_user"
        user.email = ""
        user.password = ""
        user.roles = mutableSetOf();
        this.entityManager.persist(user)
        this.entityManager.flush();
    }
}