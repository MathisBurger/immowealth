package de.immowealth.service

import de.immowealth.repository.UserRepository
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.jwt.build.JwtSignatureException
import jakarta.inject.Inject
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

/**
 * Tests for security service
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class SecurityServiceTest : AbstractServiceTest() {

    @Inject
    override lateinit var securityContext: JsonWebToken;

    @Inject
    lateinit var securityService: SecurityService;

    @Inject
    lateinit var userRepository: UserRepository;

    @Test
    @Order(1)
    fun testResetPasswordOfUnknownUser() {
        this.securityService.resetPassword("unknownUser");
        assertTrue(true);
    }

    @Test
    @Order(2)
    fun testResetPasswordOfExistingUser() {
        try {
            this.securityService.resetPassword("admin");
        } catch (_: JwtSignatureException) {}
        assertTrue(true);
    }

    @Test
    @Order(3)
    fun setNewPasswordWithoutLogin() {
        // Set password action does not throw an exception to prevent session stealing
        this.securityService.setNewPassword(-1, "12345678");
        assertTrue(true);
    }

    @Test
    @Order(4)
    fun setNewPasswordWithLogin() {
        this.loginAsUser("admin");
        val user = this.userRepository.findByUserName("admin").get();
        this.securityService.setNewPassword(user.id!!, "12345678");
        assertTrue(true);
    }
}