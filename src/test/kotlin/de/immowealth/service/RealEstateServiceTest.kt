package de.immowealth.service

import de.immowealth.data.input.CreditInput
import de.immowealth.data.input.RealEstateInput
import de.immowealth.data.input.UpdateRealEstateInput
import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.opentest4j.AssertionFailedError
import java.util.*

/**
 * Test real estate service
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RealEstateServiceTest : AbstractServiceTest() {

    @Inject
    override lateinit var securityContext: JsonWebToken;

    @Inject
    lateinit var realEstateService: RealEstateService;

    @Inject
    lateinit var tenantService: TenantService;


    @Test
    @Order(1)
    fun testCreateObject() {
        this.loginAsUser("admin")
        this.tenantService.createTenant("object_ten1", "object_tenUser", "123", "a@a.de")
        this.loginAsUser("object_tenUser")
        val obj = this.realEstateService.createObject(
            RealEstateInput(
                "1",
                "1",
                "1",
                10000L,
                CreditInput(10000L, 2.0, 2.0, "Bank"),
                Date(),
                2.0,
                24.0,
                "type",
                2000,
                2000,
                "A+",
                2.0,
                true,
                true,
                "Gas",
                "Notes"
            )
        );
        assertEquals(obj.zip, "1")
    }

    @Test
    @Order(2)
    fun testCreateObjectWithoutLogin() {
        this.logout();
        try {
            val obj = this.realEstateService.createObject(
                RealEstateInput(
                    "1",
                    "1",
                    "1",
                    10000L,
                    CreditInput(10000L, 2.0, 2.0, "Bank"),
                    Date(),
                    2.0,
                    24.0,
                    "type",
                    2000,
                    2000,
                    "A+",
                    2.0,
                    true,
                    true,
                    "Gas",
                    "Notes"
                )
            );
            fail<String>("Should not be able to create object")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(3)
    fun testGetAllObjects() {
        this.loginAsUser("object_tenUser")
        val res = this.realEstateService.getAllObjects();
        assertEquals(res.size, 1)
    }

    @Test
    @Order(4)
    fun testGetAllObjectsWithoutLogin() {
        this.logout();
        val objs = this.realEstateService.getAllObjects();
        assertEquals(objs.size, 0)
    }

    @Test
    @Order(5)
    fun testGetObject() {
        this.loginAsUser("object_tenUser")
        val objs = this.realEstateService.getAllObjects();
        val obj = this.realEstateService.getObject(objs.get(0).id!!)
        assertNotNull(obj)
    }

    @Test
    @Order(6)
    fun testGetObjectWithoutLogin() {
        this.loginAsUser("object_tenUser")
        val objs = this.realEstateService.getAllObjects();
        this.logout();
        try {
            this.realEstateService.getObject(objs.get(0).id!!);
            fail<String>("Should not be able to get all objects")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(7)
    fun testUpdateObject() {
        this.loginAsUser("object_tenUser");
        val objs = this.realEstateService.getAllObjects();
        val updated = this.realEstateService.updateObject(UpdateRealEstateInput(objs.get(0).id!!, "2", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        assertEquals(updated.realEstate.city, "2")
    }

    @Test
    @Order(8)
    fun testUpdateObjectWithoutLogin() {
        this.loginAsUser("object_tenUser");
        val objs = this.realEstateService.getAllObjects();
        this.logout();
        try {
            this.realEstateService.updateObject(UpdateRealEstateInput(objs.get(0).id!!, "2", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
            throw AssertionFailedError()
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(9)
    fun testDeleteObjectWithoutLogin() {
        this.loginAsUser("object_tenUser");
        val objs = this.realEstateService.getAllObjects();
        this.logout();
        try {
            this.realEstateService.deleteObject(objs.get(0).id!!);
            fail<String>("Should not be able to delete object")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(10)
    fun testDeleteObject() {
        this.loginAsUser("object_tenUser");
        val objs = this.realEstateService.getAllObjects();
        this.realEstateService.deleteObject(objs.get(0).id!!);
        assertTrue(true)
    }
}