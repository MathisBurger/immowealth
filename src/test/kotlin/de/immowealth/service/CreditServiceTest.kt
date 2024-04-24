package de.immowealth.service

import de.immowealth.data.input.CreditInput
import de.immowealth.data.input.RealEstateInput
import de.immowealth.data.input.UpdateCreditInput
import de.immowealth.entity.enum.AutoPayInterval
import de.immowealth.repository.CreditRepository
import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.graphql.GraphQLException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.opentest4j.AssertionFailedError
import java.util.*

/**
 * The tests for the credit service
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CreditServiceTest : AbstractServiceTest() {

    companion object {
        var creditId = 0L;
    }

    @Inject
    override lateinit var securityContext: SecurityContext;

    @Inject
    lateinit var securityService: SecurityService;

    @Inject
    lateinit var creditService: CreditService;

    @Inject
    lateinit var realEstateService: RealEstateService;

    @Inject
    lateinit var tenantService: TenantService;

    @Inject
    lateinit var creditRepository: CreditRepository;

    @Test
    @Order(1)
    fun testGetAllCredits() {
        this.loginAsUser("admin");
        this.tenantService.createTenant("ten", "credit_tenUser", "123", "a@a.de");
        this.loginAsUser("credit_tenUser");
        this.createCredit();
        val credits = this.creditService.getAllCredits();
        assertEquals(1, credits.size)
    }

    @Test
    @Order(2)
    fun testGetAllCreditsWithoutLogin() {
        this.logout();
        val credits = this.creditService.getAllCredits();
        assertEquals(0, credits.size)
    }

    @Test
    @Order(3)
    fun testGetCredit() {
        this.loginAsUser("credit_tenUser")
        val credit = this.creditService.getCredit(creditId);
        assertEquals(credit.credit.id, creditId);
    }

    @Test
    @Order(4)
    fun testGetCreditAsOtherUser() {
        this.loginAsUser("admin")
        this.tenantService.createTenant("ten2", "credit_tenUser2", "123", "ab@a.de");
        this.loginAsUser("credit_tenUser2");
        try {
            this.creditService.getCredit(creditId);
            fail<String>("Should have thrown an exception")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(5)
    fun testGetCreditWithoutLogin() {
        this.logout();
        try {
            this.creditService.getCredit(creditId);
            fail<String>("Should have thrown an exception")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(6)
    fun testUpdateCredit() {
        this.loginAsUser("credit_tenUser");
        val updated = this.creditService.updateCredit(UpdateCreditInput(creditId, 1L, null, null, null))
        assertEquals(updated.credit.amount, 1L)
    }

    @Test
    @Order(7)
    fun testUpdateCreditAsOtherUser() {
        this.loginAsUser("credit_tenUser2");
        try {
            this.creditService.updateCredit(UpdateCreditInput(creditId, 1L, null, null, null))
            fail<String>("Should have thrown an exception");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(8)
    fun testUpdateCreditWithoutLogin() {
        this.logout();
        try {
            this.creditService.updateCredit(UpdateCreditInput(creditId, 1L, null, null, null))
            fail<String>("Should have thrown an exception");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(9)
    fun testAddCreditRate() {
        this.loginAsUser("credit_tenUser");
        this.creditService.addCreditRate(creditId, 300.0, Date(), "Note");
        assertTrue(true)
    }

    @Test
    @Order(10)
    fun testAddCreditRateAsOtherUser() {
        this.loginAsUser("credit_tenUser2");
        try {
            this.creditService.addCreditRate(creditId, 300.0, Date(), "Note");
            fail<String>("Should have thrown an exception");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(11)
    fun testAddCreditRateWithoutLogin() {
        this.logout();
        try {
            this.creditService.addCreditRate(creditId, 300.0, Date(), "Note");
            fail<String>("Should have thrown an exception");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(12)
    fun testConfigureAutoBooking() {
        this.loginAsUser("credit_tenUser");
        this.creditService.configureAutoBooking(creditId, true, AutoPayInterval.MONTHLY, 300.0, Date());
        assertTrue(true);
    }

    @Test
    @Order(13)
    fun testConfigureAutoBookingAsOtherUser() {
        this.loginAsUser("credit_tenUser2");
        try {
            this.creditService.configureAutoBooking(creditId, true, AutoPayInterval.MONTHLY, 300.0, null);
            fail<String>("Should have thrown an exception");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        } catch (e: GraphQLException) {
            assertTrue(true)
        }
    }

    @Test
    @Order(14)
    fun testConfigureAutoBookingWithoutLogin() {
        this.logout()
        try {
            this.creditService.configureAutoBooking(creditId, true, AutoPayInterval.MONTHLY, 300.0, null);
            fail<String>("Should have thrown an exception");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }  catch (e: GraphQLException) {
            assertTrue(true);
        }
    }

    @Test
    @Order(15)
    fun testDeleteCreditRateWithoutLogin() {
        this.logout()
        val credit = this.creditRepository.findById(creditId)
        try {
            this.creditService.deleteCreditRate(credit.rates.get(0).id ?: -1L)
            fail<String>("Should have thrown an exception");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(16)
    fun testDeleteCreditRateAsOtherUser() {
        this.loginAsUser("credit_tenUser2");
        val credit = this.creditRepository.findById(creditId)
        try {
            this.creditService.deleteCreditRate(credit.rates.get(0).id ?: -1L)
            fail<String>("Should have thrown an exception");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(17)
    fun testDeleteCreditRate() {
        this.loginAsUser("credit_tenUser")
        val credit = this.creditRepository.findById(creditId)
        this.creditService.deleteCreditRate(credit.rates.get(0).id ?: -1L)
        assertTrue(true)
    }

    /**
     * Creates a credit for testing
     */
    @Transactional
    fun createCredit() {
        val obj = this.realEstateService.createObject(RealEstateInput(
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
        creditId = obj.credit?.id ?: -1L;
    }
}