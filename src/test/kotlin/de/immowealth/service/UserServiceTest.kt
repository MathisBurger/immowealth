package de.immowealth.service

import de.immowealth.entity.UserRoles
import de.immowealth.repository.UserRepository
import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.ws.rs.core.SecurityContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

/**
 * Tests the functionality of the user service
 */
@QuarkusTest
class UserServiceTest : AbstractServiceTest() {

    @Inject
    lateinit var userService: UserService;

    @Inject
    lateinit var entityManager: EntityManager;

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    lateinit var tenantService: TenantService;

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
                mutableListOf(UserRoles.ADMIN),
                null
            );
            fail<String>("Failed to create user")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    fun testRegisterUserAsUnknownUser() {
        this.removeAllUsersExceptAdmin(userRepository, entityManager);
        this.loginAsUser("admin2");
        try {
            this.userService.registerUser(
                "admin3",
                "123",
                "test@test.de",
                mutableListOf(UserRoles.ADMIN)
            );
            fail<String>("Failed to create user")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    fun testCreateUserAsTenantOwner() {
        this.removeAllUsersExceptAdmin(userRepository, entityManager);
        this.loginAsUser("admin");
        val ten = this.tenantService.createTenant("ten1", "owner", "123", "owner@chef.de");
        this.loginAsUser("owner");
        val usr = this.userService.registerUser("usr", "123", "a@chef.de", mutableListOf(), ten.id);
        assertEquals(usr.username, "usr")
        assertEquals(usr.tenant?.id ?: -1, ten.id)
    }

}