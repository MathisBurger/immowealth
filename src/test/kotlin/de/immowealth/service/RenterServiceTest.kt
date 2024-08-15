package de.immowealth.service

import de.immowealth.data.input.CreateRenterInput
import de.immowealth.data.input.CreditInput
import de.immowealth.data.input.RealEstateInput
import de.immowealth.entity.UserRoles
import de.immowealth.exception.ParameterException
import de.immowealth.repository.RealEstateRepository
import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.*

/**
 * Tests the renter service
 */
@QuarkusTest
class RenterServiceTest() : AbstractServiceTest() {

    @Inject
    override lateinit var securityContext: JsonWebToken;

    @Inject
    lateinit var tenantService: TenantService

    @Inject
    lateinit var realEstateRepository: RealEstateRepository

    @Inject
    lateinit var realEstateService: RealEstateService;

    @Inject
    lateinit var renterService: RenterService;

    @Test
    @Order(1)
    fun testCreateRenterAsAdmin() {
        this.createObjectWithTenant();
        this.loginAsUser("admin");
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        val res = this.renterService.createRenterOnObject(CreateRenterInput(
            obj.id!!,
            "Test",
            "User",
            Date(),
            "123",
            "1",
            "1"
        ));
        assertNotNull(res);
    }

    @Test
    @Order(2)
    fun testCreateRenterAsUserAssigned() {
        this.loginAsUser("ten1_renter_user")
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        val res = this.renterService.createRenterOnObject(CreateRenterInput(
            obj.id!!,
            "Test",
            "User",
            Date(),
            "2",
            "2",
            "2"
        ));
        assertNotNull(res);
    }

    @Test
    @Order(3)
    fun testCreateRenterAsUserUnassigned() {
        this.loginAsUser("ten_test_renter_unass");
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        assertThrows(UnauthorizedException::class.java) {
            this.renterService.createRenterOnObject(CreateRenterInput(
                obj.id!!,
                "Test",
                "User",
                Date(),
                "3",
                "3",
                "3"
            ));
        }
    }

    @Test
    @Order(4)
    fun testCreateRenterAsUnknownUser() {
        this.loginAsUser("admin");
        val obj = this.realEstateRepository.findByCity("obj_renter_01");
        this.loginAsUser("unknown")
        assertThrows(UnauthorizedException::class.java) {
            this.renterService.createRenterOnObject(CreateRenterInput(
                obj.id!!,
                "Test",
                "User",
                Date(),
                "4",
                "4",
                "4"
            ));
        }
    }

    @Test
    @Order(5)
    fun testCreateObjectWithInvalidParams() {
        this.loginAsUser("admin");
        assertThrows(ParameterException::class.java) {
            this.renterService.createRenterOnObject(CreateRenterInput(
                -1,
                "Test",
                "User",
                Date(),
                "5",
                "5",
                "5"
            ));
        }
    }

    @Test
    @Order(6)
    fun testDeleteRenterFromObjectAsAdmin() {
        this.loginAsUser("admin");
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        val renter = obj.renters.first();
        assertDoesNotThrow {
            this.renterService.deleteRenterFromObject(renter.id!!)
        }
    }

    @Test
    @Order(7)
    fun testDeleteRenterFromObjectAsAssigned() {
        this.loginAsUser("ten1_renter_user")
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        val renter = obj.renters.first();
        assertDoesNotThrow {
            this.renterService.deleteRenterFromObject(renter.id!!)
        }
    }

    @Test
    @Order(8)
    fun testDeleteRenterFromObjectAsUnassigned() {
        this.loginAsUser("ten_test_renter_unass");
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        val renter = obj.renters.first();
        assertThrows(UnauthorizedException::class.java) {
            this.renterService.deleteRenterFromObject(renter.id!!)
        }
    }

    @Test
    @Order(9)
    fun testDeleteRenterFromObjectAsUnknown() {
        this.loginAsUser("unknown");
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        val renter = obj.renters.first();
        assertThrows(UnauthorizedException::class.java) {
            this.renterService.deleteRenterFromObject(renter.id!!)
        }
    }

    @Test
    @Order(10)
    fun testDeleteRenterFromObjectWithInvalidParams() {
        this.loginAsUser("admin");
        assertThrows(ParameterException::class.java) {
            this.renterService.deleteRenterFromObject(-1);
        }
    }
    @Test
    @Order(11)
    fun testUnassignRenterFromObjectAsUnknown() {
        this.loginAsUser("admin");
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        val res = this.renterService.createRenterOnObject(CreateRenterInput(
            obj.id!!,
            "Test",
            "User",
            Date(),
            "1",
            "1",
            "1"
        ));
        this.logout();
        assertThrows(UnauthorizedException::class.java) {
            this.renterService.unassignRenterFromObject(res.id!!)
        }
    }

    @Test
    @Order(12)
    fun testUnassignRenterFromObjectAsAdmin() {
        this.loginAsUser("admin");
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        assertDoesNotThrow {
            this.renterService.unassignRenterFromObject(obj.renters.first().id!!)
        }
    }

    @Test
    @Order(13)
    fun testGetUnassignedRentersAsAdmin() {
        this.loginAsUser("admin");
        val renters = this.renterService.getUnassignedRenters();
        assertTrue(renters.isNotEmpty());
    }

    @Test
    @Order(14)
    fun testGetUnassignedRentersAsUnknown() {
        this.logout();
        val renters = this.renterService.getUnassignedRenters();
        assertTrue(renters.isEmpty());
    }

    @Test
    @Order(15)
    fun testAssignRentersAsUnknown() {
        this.loginAsUser("admin");
        val renter = this.renterService.getUnassignedRenters().first();
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        this.logout();
        assertThrows(UnauthorizedException::class.java) {
            this.renterService.assignRenterToObject(renter.id!!, obj.id!!)
        }
    }

    @Test
    @Order(16)
    fun testAssignRenterAsAdmin() {
        this.loginAsUser("admin");
        val renter = this.renterService.getUnassignedRenters().first();
        val obj = this.realEstateRepository.findByCity("obj_renter_01")
        assertDoesNotThrow {
            this.renterService.assignRenterToObject(renter.id!!, obj.id!!)
        }
    }


    private fun createObjectWithTenant() {
        this.loginAsUser("admin")
        this.tenantService.createTenant("ten1_renter", "ten1_renter_user", "1", "a@a.de")
        this.tenantService.userService.registerUser("ten_test_renter_unass", "ten1_renter_user2", "a@b.de", mutableSetOf(UserRoles.TENANT_ASSIGNED))
        this.loginAsUser("ten1_renter_user")
        val obj = this.realEstateService.createObject(RealEstateInput(
            "obj_renter_01",
            "",
            "",
            1,
            CreditInput(1, 1.0, 1.0, ""),
            Date(),
            1.0,
            12.0,
            "HOUSE",
            2000,
            2000,
            "A++",
            2.0,
            true,
            true,
            "GAS",
            ""
        ));
        println("Creates object with city: " + obj.city);
        this.logout();
    }
}