package de.mathisburger.service

import de.mathisburger.data.response.CreditResponse
import de.mathisburger.entity.Credit
import de.mathisburger.entity.CreditRate
import de.mathisburger.entity.RealEstateObject
import de.mathisburger.entity.enum.AutoPayInterval
import de.mathisburger.exception.DateNotAllowedException
import de.mathisburger.repository.CreditRateRepository
import de.mathisburger.repository.CreditRepository
import de.mathisburger.repository.RealEstateRepository
import de.mathisburger.util.AutoBookingUtils
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.eclipse.microprofile.graphql.GraphQLException
import java.util.Date

/**
 * The credit service
 */
@ApplicationScoped
class CreditService {

    @Inject
    lateinit var creditRepository: CreditRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    @Inject
    lateinit var creditRateRepository: CreditRateRepository;

    @Inject
    lateinit var realEstateRepository: RealEstateRepository;

    /**
     * Adds a new credit rate
     *
     * @param id The ID of the credit
     * @param rate The credit rate amount
     * @param date The date of booking
     */
    @Transactional
    fun addCreditRate(id: Long, rate: Double, date: Date) {
        if (date.after(Date())) {
            throw DateNotAllowedException("Date in future is not allowed");
        }
        val credit = this.creditRepository.findById(id);
        val creditRate = CreditRate()
        creditRate.date = date;
        creditRate.amount = rate;
        this.entityManager.persist(creditRate);
        credit.rates.add(creditRate);
        this.entityManager.persist(credit);
        this.entityManager.flush();
    }

    /**
     * Gets all credits
     */
    fun getAllCredits(): List<CreditResponse> {
        val credits = this.creditRepository.listAll();
        val transform: (Credit) -> CreditResponse = {this.getResponseObject(it)};
        return credits.map(transform);
    }

    /**
     * Gets a specific credit
     *
     * @param id The ID of the credit
     */
    fun getCredit(id: Long): CreditResponse {
        val credit = this.creditRepository.findById(id);
        return this.getResponseObject(credit);
    }

    /**
     * Configures auto booking
     *
     * @param id The credit ID
     * @param enabled Enabled status of auto booking
     * @param interval The booking interval
     * @param amount The booking amount of money per interval
     */
    @Transactional
    fun configureAutoBooking(id: Long, enabled: Boolean, interval: AutoPayInterval?, amount: Double?): CreditResponse {
        var credit = this.creditRepository.findById(id);
        if (!enabled) {
            credit.autoPayInterval = null;
            credit.nextCreditRate = null;
            credit.autoPayAmount = null;
            this.entityManager.persist(credit);
            this.entityManager.flush();
            return this.getResponseObject(credit);
        }
        if (interval == null) {
            throw GraphQLException("Interval must be configured if auto booking is enabled")
        }
        if (amount == null) {
            throw GraphQLException("Amount must be configured if auto booking is enabled")
        }
        credit.autoPayInterval = interval;
        credit.nextCreditRate = AutoBookingUtils.getNextAutoPayIntervalDate(interval);
        credit.autoPayAmount = amount;
        this.entityManager.persist(credit);
        this.entityManager.flush();
        return this.getResponseObject(credit);
    }

    /**
     * Deletes a credit rate
     *
     * @param id The id of the credit rate
     */
    @Transactional
    fun deleteCreditRate(id: Long) {
        val obj = this.creditRateRepository.findById(id);
        this.entityManager.remove(obj);
        this.entityManager.flush();
    }

    /**
     * Gets the credit response of a credit
     *
     * @param credit The credit entity
     */
    private fun getResponseObject(credit: Credit): CreditResponse {
        var sum = 0.0;
        var list: MutableList<Double> = mutableListOf();
        for (rate in credit.rates) {
            sum += rate.amount!!;
            list.add(sum);
        }
       val res =
        return CreditResponse(credit, sum, list, this.realEstateRepository.getByCredit(credit).id!!);
    }
}