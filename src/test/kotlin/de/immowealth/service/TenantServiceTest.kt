package de.immowealth.service

import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

/**
 * Tests the functionality of the tenant service
 */
@QuarkusTest
class TenantServiceTest : AbstractServiceTest() {

    @Inject
    override lateinit var securityContext: JsonWebToken;

    @Inject
    lateinit var tenantService: TenantService;

    @Inject
    lateinit var userService: UserService;

    @Test
    fun testCreateTenantAsAdmin() {
        this.loginAsUser("admin");
        val ten = this.tenantService.createTenant("ten1", "usr", "pwd", "test@test.de");
        assertEquals(ten.name, "ten1")
    }

    @Test
    fun testCreateTenantAsUnknownUser() {
        this.logout();
        try {
            this.tenantService.createTenant("ren2", "usr2", "pwd", "test@test.de")
            fail<String>("Cannot create tenant without login")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    fun testCreateTenantAsNonAdmin() {
        this.loginAsUser("admin");
        this.userService.registerUser("nonAdmin", "123", "a@a.a", mutableListOf());
        try {
            this.loginAsUser("nonAdmin")
            this.tenantService.createTenant("ren1", "usr", "pwd", "test@test.de");
            fail<String>("Cannot create as non admin")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }
}