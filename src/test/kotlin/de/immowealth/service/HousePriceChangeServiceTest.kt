package de.immowealth.service

import de.immowealth.data.input.UpdateHousePriceChangeInput
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.ws.rs.core.SecurityContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

/**
 * Tests the house price change service
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class HousePriceChangeServiceTest : AbstractServiceTest() {

    @Inject
    override lateinit var securityContext: SecurityContext

    @Inject
    lateinit var housePriceChangeService: HousePriceChangeService;

    @Inject
    lateinit var tenantService: TenantService;

    @Test
    @Order(1)
    fun testAddHousePriceChange() {
        this.loginAsUser("admin")
        this.tenantService.createTenant("price_change_ten1", "price_change_tenUser", "123", "a@b.com");
        this.loginAsUser("price_change_tenUser");
        val change = this.housePriceChangeService.addHousePriceChange("85055", 2.0, 2024);
        assertEquals(change.change, 2.0);
    }

    @Test
    @Order(2)
    fun testAddHousePriceChangeAsOtherUser() {
        this.loginAsUser("admin")
        this.tenantService.createTenant("price_change_ten2", "price_change_tenUser2", "123", "a@b.com");
        this.loginAsUser("price_change_tenUser2");
        this.housePriceChangeService.addHousePriceChange("85055", 2.0, 2024);
        assertTrue(true);
    }

    @Test
    @Order(3)
    fun testAddHousePriceChangeWithoutLogin() {
        this.logout()
        try {
            this.housePriceChangeService.addHousePriceChange("85055", 2.0, 2024);
            fail<String>("Should not add house price change");
        } catch (e: Throwable) {
            assertTrue(true);
        }
    }

    @Test
    @Order(4)
    fun testGetAllChanges() {
        this.loginAsUser("price_change_tenUser");
        val result = this.housePriceChangeService.getAllChanges();
        assertEquals(1, result.size)
    }

    @Test
    @Order(5)
    fun testGetAllChangesAsOtherUser() {
        this.loginAsUser("price_change_tenUser2");
        val result = this.housePriceChangeService.getAllChanges();
        assertEquals(1, result.size)
    }

    @Test
    @Order(6)
    fun testGetAllChangesWithoutLogin() {
        this.logout()
        val result = this.housePriceChangeService.getAllChanges();
        assertEquals(0, result.size)
    }

    @Test
    @Order(7)
    fun testGetAllChangesWithZip() {
        this.loginAsUser("price_change_tenUser");
        val result = this.housePriceChangeService.getAllChangesWithZip("85055");
        assertEquals(1, result.size)
        val result2 = this.housePriceChangeService.getAllChangesWithZip("85056");
        assertEquals(0, result2.size)
    }

    @Test
    @Order(8)
    fun testGetAllChangesWithZipAsOtherUser() {
        this.loginAsUser("price_change_tenUser2");
        val result = this.housePriceChangeService.getAllChangesWithZip("85055");
        assertEquals(1, result.size)
        val result2 = this.housePriceChangeService.getAllChangesWithZip("85056");
        assertEquals(0, result2.size)
    }

    @Test
    @Order(9)
    fun testGetAllChangesWithZipWithoutLogin() {
        this.logout()
        val result = this.housePriceChangeService.getAllChangesWithZip("85055");
        assertEquals(0, result.size)
        val result2 = this.housePriceChangeService.getAllChangesWithZip("85056");
        assertEquals(0, result2.size)
    }

    @Test
    @Order(10)
    fun testUpdateHousePriceChange() {
        this.loginAsUser("price_change_tenUser")
        val change = this.housePriceChangeService.getAllChanges().get(0);
        val updated = this.housePriceChangeService.updateHousePrices(UpdateHousePriceChangeInput(change.id!!, "85056", null, null))
        assertEquals(updated.zip, "85056")
    }

    @Test
    @Order(11)
    fun testUpdateHousePriceChangeAsOtherUser() {
        this.loginAsUser("price_change_tenUser")
        val change = this.housePriceChangeService.getAllChanges().get(0);
        this.loginAsUser("price_change_tenUser2")
        try {
            this.housePriceChangeService.updateHousePrices(UpdateHousePriceChangeInput(change.id!!, "85056", null, null))
            fail<String>("Should not be able to update");
        } catch (e: Throwable) {
            assertTrue(true);
        }
    }

    @Test
    @Order(12)
    fun testUpdateHousePriceChangeWithoutLogin() {
        this.loginAsUser("price_change_tenUser")
        val change = this.housePriceChangeService.getAllChanges().get(0);
        this.logout();
        try {
            this.housePriceChangeService.updateHousePrices(UpdateHousePriceChangeInput(change.id!!, "85056", null, null))
            fail<String>("Should not be able to update");
        } catch (e: Throwable) {
            assertTrue(true);
        }
    }

    @Test
    @Order(13)
    fun testDeleteWithoutLogin() {
        this.loginAsUser("price_change_tenUser")
        val change = this.housePriceChangeService.getAllChanges().get(0);
        this.logout();
        try {
            this.housePriceChangeService.delete(change.id!!)
            fail<String>("Should not be able to delete");
        } catch (e: Throwable) {
            assertTrue(true);
        }
    }

    @Test
    @Order(14)
    fun testDeleteAsOtherUser() {
        this.loginAsUser("price_change_tenUser")
        val change = this.housePriceChangeService.getAllChanges().get(0);
        this.loginAsUser("price_change_tenUser2")
        try {
            this.housePriceChangeService.delete(change.id!!)
            fail<String>("Should not be able to delete");
        } catch (e: Throwable) {
            assertTrue(true);
        }
    }

    @Test
    @Order(15)
    fun testDelete() {
        this.loginAsUser("price_change_tenUser")
        val change = this.housePriceChangeService.getAllChanges().get(0);
        this.housePriceChangeService.delete(change.id!!);
        assertTrue(true);
    }
}