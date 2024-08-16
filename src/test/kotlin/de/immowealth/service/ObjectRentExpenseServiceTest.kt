package de.immowealth.service

import de.immowealth.data.input.CreditInput
import de.immowealth.data.input.RealEstateInput
import de.immowealth.entity.enum.ObjectRentType
import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
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
 * Tests the object rent expense service
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ObjectRentExpenseServiceTest : AbstractServiceTest() {

    companion object {
        var objectId = -1L
    }

    @Inject
    override lateinit var securityContext: JsonWebToken

    @Inject
    lateinit var objectRentExpenseService: ObjectRentExpenseService;

    @Inject
    lateinit var objectService: RealEstateService;

    @Inject
    lateinit var tenantService: TenantService;

    @Test
    @Order(1)
    fun testAddObjectRentExpense() {
        this.loginAsUser("admin")
        this.tenantService.createTenant("expense_ten1", "expense_tenUser", "123", "a@b.com");
        this.loginAsUser("expense_tenUser")
        this.createObject();
        val expense = this.objectRentExpenseService.addObjectRentExpenseToObject(objectId, 200.0, ObjectRentType.RESERVE, "expense");
        assertEquals(ObjectRentType.RESERVE, expense.type)
    }

    @Test
    @Order(2)
    fun testAddObjectRentExpenseAsOtherUser() {
        this.loginAsUser("admin")
        this.tenantService.createTenant("expense_ten2", "expense_tenUser2", "123", "a@b.com");
        this.loginAsUser("expense_tenUser2")
        try {
            this.objectRentExpenseService.addObjectRentExpenseToObject(objectId ?: -1L, 200.0, ObjectRentType.RESERVE, "expense");
            fail<String>("Should not be able to add rent expense");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(3)
    fun testAddObjectRentExpenseWithoutLogin() {
        this.logout();
        try {
            this.objectRentExpenseService.addObjectRentExpenseToObject(objectId ?: -1L, 200.0, ObjectRentType.RESERVE, "expense");
            fail<String>("Should not be able to add rent expense");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(4)
    fun testUpdateRentExpense() {
        this.loginAsUser("admin");
        val obj = this.objectService.getObject(objectId);
        assertNotNull(obj);
        val expense = obj.realEstate.expenses[0];
        this.loginAsUser("expense_tenUser");
        val updated = this.objectRentExpenseService.updateRentExpense(expense.id!!, 300.0, null, null)
        assertEquals(updated.value, 300.0)
    }

    @Test
    @Order(5)
    fun testUpdateRentExpenseAsOtherUser() {
        this.loginAsUser("admin")
        val expense = this.objectService.getObject(objectId).realEstate.expenses.get(0);
        this.loginAsUser("expense_tenUser2");
        try {
            this.objectRentExpenseService.updateRentExpense(expense.id!!, 300.0, null, null)
            fail<String>("Should not be able to update rent expense");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(6)
    fun testUpdateRentExpenseWithoutLogin() {
        this.loginAsUser("admin")
        val expense = this.objectService.getObject(objectId).realEstate.expenses.get(0);
        this.logout()
        try {
            this.objectRentExpenseService.updateRentExpense(expense.id!!, 300.0, null, null)
            fail<String>("Should not be able to update rent expense");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(7)
    fun testDeleteRentExpenseWithoutLogin() {
        this.loginAsUser("admin")
        val expense = this.objectService.getObject(objectId).realEstate.expenses.get(0);
        this.logout()
        try {
            this.objectRentExpenseService.deleteExpense(expense.id!!)
            fail<String>("Should not be able to update rent expense");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(8)
    fun testDeleteRentExpenseAsOtherUser() {
        this.loginAsUser("admin")
        val expense = this.objectService.getObject(objectId).realEstate.expenses.get(0);
        this.loginAsUser("expense_tenUser2");
        try {
            this.objectRentExpenseService.deleteExpense(expense.id!!)
            fail<String>("Should not be able to update rent expense");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(9)
    fun testSetAutoBookingByExpenses() {
        this.loginAsUser("expense_tenUser")
        this.objectRentExpenseService.setAutoBookingByExpenses(objectId)
        assertTrue(true)
    }

    @Test
    @Order(10)
    fun testSetAutoBookingByExpensesAsOtherUser() {
        this.loginAsUser("expense_tenUser2")
        try {
            this.objectRentExpenseService.setAutoBookingByExpenses(objectId)
            fail<String>("Should not be able to set auto booking by expenses");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(11)
    fun testSetAutoBookingByExpensesWithoutLogin() {
        this.logout()
        try {
            this.objectRentExpenseService.setAutoBookingByExpenses(objectId)
            fail<String>("Should not be able to set auto booking by expenses");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(12)
    fun testDeleteObjectRentExpense() {
        this.loginAsUser("admin")
        val expense = this.objectService.getObject(objectId).realEstate.expenses.get(0);
        this.loginAsUser("expense_tenUser");
        this.objectRentExpenseService.deleteExpense(expense.id!!)
        assertTrue(true);
    }

    /**
     * Creates a credit for testing
     */
    @Transactional
    fun createObject() {
        val obj = this.objectService.createObject(
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
        objectId = obj.id ?: -1L;
    }
}