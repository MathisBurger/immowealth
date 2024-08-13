package de.immowealth.service

import de.immowealth.entity.UserRoles
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
        this.userService.registerUser("nonAdmin", "123", "a@a.a", mutableSetOf());
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
        println(tenants);
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

    @Test
    @Order(10)
    fun testMoveUsersAsAdmin() {
        this.loginAsUser("admin");
        var ten1 = this.tenantService.createTenant("mv_1", "mv1", "123", "test@test.de");
        var ten2 = this.tenantService.createTenant("mv_2", "mv2", "123", "test@test.de");
        val usr = this.userService.registerUser("mv3", "123", "a@a.a", mutableSetOf(UserRoles.TENANT_ASSIGNED), ten1.id);
        ten1 = this.tenantService.getTenant(ten1.id!!)
        assertEquals(ten1.users.size,2);
        ten2 = this.tenantService.moveUsersBetweenTenant(ten2.id!!, mutableListOf(usr.id!!, ten2.users.get(0).id!!));
        assertEquals(ten2.users.size, 2);
    }

    @Test
    @Order(11)
    fun testMoveUsersAsTenantOwner() {
        this.loginAsUser("admin");
        var ten1 = this.tenantService.createTenant("mv_11", "mv11", "123", "test@test.de");
        val ten2 = this.tenantService.createTenant("mv_21", "mv21", "123", "test@test.de");
        val usr = this.userService.registerUser("mv31", "123", "a@a.a", mutableSetOf(UserRoles.TENANT_ASSIGNED), ten1.id);
        ten1 = this.tenantService.getTenant(ten1.id!!)
        assertEquals(ten1.users.size,2);
        this.loginAsUser("mv21");
        try {
            this.tenantService.moveUsersBetweenTenant(ten2.id!!, mutableListOf(usr.id!!, ten2.users.get(0).id!!));
            fail<String>("This should not be allowed");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(12)
    fun testMoveUsersAsRandomUser() {
        this.loginAsUser("admin");
        var ten1 = this.tenantService.createTenant("mv_11", "mv112", "123", "test@test.de");
        val ten2 = this.tenantService.createTenant("mv_21", "mv212", "123", "test@test.de");
        val usr = this.userService.registerUser("mv31", "123", "a@a.a", mutableSetOf(UserRoles.TENANT_ASSIGNED), ten1.id);
        ten1 = this.tenantService.getTenant(ten1.id!!)
        assertEquals(ten1.users.size,2);
        this.loginAsUser("mv31");
        try {
            this.tenantService.moveUsersBetweenTenant(ten2.id!!, mutableListOf(usr.id!!, ten2.users.get(0).id!!));
            fail<String>("This should not be allowed");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }
}