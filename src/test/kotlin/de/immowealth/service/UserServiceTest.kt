package de.immowealth.service

import de.immowealth.entity.UserRoles
import de.immowealth.repository.UserRepository
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.ws.rs.core.SecurityContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@QuarkusTest
class UserServiceTest : AbstractServiceTest() {

    @Inject
    lateinit var userService: UserService;

    @Inject
    lateinit var entityManager: EntityManager;

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    override lateinit var securityContext: SecurityContext;

    @Test
    fun testRegisterUserAsAdmin() {
        this.removeAllUsersExceptAdmin(userRepository, entityManager);
        this.loginAsUser("admin");
        val user = this.userService.registerUser(
            "admin2",
            "123",
            "test@test.de",
            mutableListOf(UserRoles.ADMIN)
        );
        assertEquals(user.username, "admin2");
    }

    @Test
    fun testRegisterUserAsNonAdmin() {
        this.removeAllUsersExceptAdmin(userRepository, entityManager);
        this.loginAsUser("admin");
        this.userService.registerUser(
            "admin2",
            "123",
            "test@test.de",
            mutableListOf(UserRoles.TENANT_OWNER)
        );
        this.loginAsUser("admin2");
        try {
            this.userService.registerUser(
                "admin3",
                "123",
                "test@test.de",
                mutableListOf(UserRoles.ADMIN)
            );
            fail<String>("Failed to create user")
        } catch (_: Throwable) {
            assertTrue(true);
        }
    }

    @Test
    fun testRegisterUserAsUnknownUser() {
        this.removeAllUsersExceptAdmin(userRepository, entityManager);
        this.loginAsUser("admin");
        this.loginAsUser("admin2");
        try {
            this.userService.registerUser(
                "admin3",
                "123",
                "test@test.de",
                mutableListOf(UserRoles.ADMIN)
            );
            fail<String>("Failed to create user")
        } catch (_: Throwable) {
            assertTrue(true);
        }
    }

}