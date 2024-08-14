package de.immowealth.service

import de.immowealth.entity.UserRoles
import de.immowealth.repository.UserRepository
import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order
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
    override lateinit var securityContext: JsonWebToken;

    @Test
    @Order(1)
    fun testRegisterUserAsAdmin() {
        this.loginAsUser("admin");
        val user = this.userService.registerUser(
            "admin2",
            "123",
            "test@test.de",
            mutableSetOf(UserRoles.ADMIN)
        );
        assertEquals(user.username, "admin2");
    }

    @Test
    @Order(2)
    fun testRegisterUserAsNonAdmin() {
        this.loginAsUser("admin");
        this.userService.registerUser(
            "admin23",
            "123",
            "test@test.de",
            mutableSetOf(UserRoles.TENANT_OWNER)
        );
        this.loginAsUser("admin23");
        try {
            this.userService.registerUser(
                "admin3",
                "123",
                "test@test.de",
                mutableSetOf(UserRoles.ADMIN),
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
    @Order(3)
    fun testRegisterUserAsUnknownUser() {
        this.loginAsUser("admin2443434");
        try {
            this.userService.registerUser(
                "admin3345353",
                "123",
                "test@test.de",
                mutableSetOf(UserRoles.ADMIN)
            );
            fail<String>("Failed to create user")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(4)
    fun testCreateUserAsTenantOwner() {
        this.loginAsUser("admin");
        val ten = this.tenantService.createTenant("ten1", "owner123", "123", "owner@chef.de");
        this.loginAsUser("owner123");
        val usr = this.userService.registerUser("usr123", "123", "a@chef.de", mutableSetOf(), ten.id);
        assertEquals(usr.username, "usr123")
        assertEquals(usr.tenant?.id ?: -1, ten.id)
    }

}