package de.immowealth.service

import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.opentest4j.AssertionFailedError

/**
 * Tests the functionality of the tenant service
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TenantServiceTest : AbstractServiceTest() {

    @Inject
    override lateinit var securityContext: JsonWebToken;

    @Inject
    lateinit var tenantService: TenantService;

    @Inject
    lateinit var userService: UserService;

    @Test
    @Order(1)
    fun testCreateTenantAsAdmin() {
        this.loginAsUser("admin");
        val ten = this.tenantService.createTenant("ten1", "usr", "pwd", "test@test.de");
        assertEquals(ten.name, "ten1")
    }

    @Test
    @Order(2)
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
    @Order(3)
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

    @Test
    @Order(4)
    fun testGetAllTenants() {
        this.loginAsUser("admin");
        val amount = this.tenantService.getAllTenants();
        assertTrue(amount.isNotEmpty())
    }

    @Test
    @Order(5)
    fun testGetAllTenantsAsNonAdmin() {
        this.loginAsUser("nonAdmin");
        val tenants = this.tenantService.getAllTenants();
        assertTrue(tenants.isEmpty())
    }

    @Test
    @Order(6)
    fun testGetAllTenantsAsOwner() {
        this.loginAsUser("usr");
        val tenants = this.tenantService.getAllTenants();
        assertSame(1, tenants.size)
    }

    @Test
    @Order(7)
    fun testGetTenant() {
        this.loginAsUser("admin");
        val tenant = this.tenantService.getAllTenants().first();
        val fetched = this.tenantService.getTenant(tenant.id!!);
        assertSame(fetched.id, tenant.id)
    }

    @Test
    @Order(8)
    fun testGetTenantAsOwner() {
        this.loginAsUser("usr");
        val tenant = this.tenantService.getAllTenants().first();
        val fetched = this.tenantService.getTenant(tenant.id!!);
        assertSame(fetched.id, tenant.id)
    }

    @Test
    @Order(9)
    fun testGetTenantAsUnknownUser() {
        this.loginAsUser("usr");
        val tenant = this.tenantService.getAllTenants().first();
        this.logout();
        try {
            this.tenantService.getTenant(tenant.id!!);
            fail<String>("Should not be able to get tenant")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }
}