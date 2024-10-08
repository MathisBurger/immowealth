package de.immowealth.service

import de.immowealth.data.input.UpdateCreditInput
import de.immowealth.data.response.CreditResponse
import de.immowealth.entity.Credit
import de.immowealth.entity.CreditRate
import de.immowealth.entity.enum.AutoPayInterval
import de.immowealth.entity.enum.MailEntityContext
import de.immowealth.entity.enum.MailerSettingAction
import de.immowealth.exception.DateNotAllowedException
import de.immowealth.repository.CreditRateRepository
import de.immowealth.repository.CreditRepository
import de.immowealth.repository.RealEstateRepository
import de.immowealth.util.AutoBookingUtils
import de.immowealth.util.DateUtils
import de.immowealth.voter.CreditRateVoter
import de.immowealth.voter.CreditVoter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.graphql.GraphQLException
import java.util.Date

/**
 * The credit service
 */
@ApplicationScoped
class CreditService : AbstractService() {

    @Inject
    lateinit var creditRepository: CreditRepository;

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
     * @param note The note of the booking
     * @param mail If email should be sent
     */
    @Transactional
    fun addCreditRate(id: Long, rate: Double, date: Date, note: String?, mail: Boolean = true) {
        if (date.after(Date())) {
            throw DateNotAllowedException("Date in future is not allowed");
        }
        val credit = this.creditRepository.findById(id);
        this.denyUnlessGranted(CreditVoter.READ, credit);
        val creditRate = CreditRate()
        creditRate.date = date;
        creditRate.amount = this.cs.convertBack(rate);
        creditRate.note = note;
        creditRate.credit = credit;
        creditRate.tenant = credit.tenant;
        this.denyUnlessGranted(CreditRateVoter.CREATE, creditRate);
        this.entityManager.persist(creditRate);
        credit.rates.add(creditRate);
        this.entityManager.persist(credit);
        this.entityManager.flush();
        this.log.writeLog("Added credit rate (${this.cs.convertBack(rate)}€) to credit with ID ${id}");
        if (mail) {
            val currentUser = this.securityService.getCurrentUser();
            this.mail.sendEntityActionMail(
                "Added credit rate",
                "Credit rate has been added to credit with ID $id",
                currentUser?.email ?: "",
                MailEntityContext.credit,
                MailerSettingAction.UPDATE_ONLY,
                credit.isFavourite(currentUser)
            );
        }
    }

    /**
     * Gets all credits
     */
    fun getAllCredits(): List<CreditResponse> {
        val credits: List<Credit> = this.filterAccess(CreditVoter.READ, this.creditRepository.listAll());
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
        this.denyUnlessGranted(CreditVoter.READ, credit);
        return this.getResponseObject(credit);
    }

    /**
     * Updates a credit.
     *
     * @param input The update input
     * @return The updated credit
     */
    @Transactional
    fun updateCredit(input: UpdateCreditInput): CreditResponse {
        val credit = this.creditRepository.findById(input.id);
        credit.amount = input.amount ?: credit.amount;
        credit.interestRate = input.interestRate ?: credit.interestRate;
        credit.redemptionRate = input.redemptionRate ?: credit.redemptionRate;
        credit.bank = input.bank ?: credit.bank;
        this.denyUnlessGranted(CreditVoter.UPDATE, credit);
        this.entityManager.persist(credit);
        this.entityManager.flush();
        this.log.writeLog("Updated credit with ID ${credit.id}");
        val currentUser = this.securityService.getCurrentUser();
        this.mail.sendEntityActionMail(
            "Updated credit",
            "Updated credit with ID ${credit.id}",
            currentUser?.email ?: "",
            MailEntityContext.credit,
            MailerSettingAction.UPDATE_ONLY,
            credit.isFavourite(currentUser)
        );
        return this.getCredit(credit.id!!);

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
    fun configureAutoBooking(id: Long, enabled: Boolean, interval: AutoPayInterval?, amount: Double?, startDate: Date?): CreditResponse {
        var credit = this.creditRepository.findById(id);
        if (!enabled) {
            credit.autoPayInterval = null;
            credit.nextCreditRate = null;
            credit.autoPayAmount = null;
            this.denyUnlessGranted(CreditVoter.UPDATE, credit);
            this.entityManager.persist(credit);
            this.entityManager.flush();
            this.log.writeLog("Disabled auto booking for credit with ID ${credit.id}");
            return this.getResponseObject(credit);
        }
        if (interval == null) {
            throw GraphQLException("Interval must be configured if auto booking is enabled")
        }
        if (amount == null) {
            throw GraphQLException("Amount must be configured if auto booking is enabled")
        }
        if (startDate == null) {
            throw GraphQLException("Start date must be configured if auto booking is enabled")
        }
        credit.autoPayInterval = interval;
        credit.nextCreditRate = AutoBookingUtils.getNextAutoPayIntervalDate(interval);
        if (startDate.before(Date())) {
            var nextRate = AutoBookingUtils.getNextAutoPayIntervalDate(interval, DateUtils.dateToCalendar(startDate));
            while (nextRate.time < Date().time) {
                this.addCreditRate(
                    credit.id!!,
                    this.cs.convertBack(amount)!!,
                    nextRate,
                    "[AUTOBOOKING] Backward autobooking",
                    false
                    );
                nextRate = AutoBookingUtils.getNextAutoPayIntervalDate(interval, DateUtils.dateToCalendar(nextRate));
            }
            this.log.writeLog("Added backward autobooking on credit with ID ${credit.id}");
        } else {
            credit.nextCreditRate = AutoBookingUtils.getNextAutoPayIntervalDate(interval, DateUtils.dateToCalendar(startDate));
        }
        credit.autoPayAmount = this.cs.convertBack(amount);
        this.denyUnlessGranted(CreditRateVoter.UPDATE, credit);
        this.entityManager.persist(credit);
        this.entityManager.flush();
        this.log.writeLog("Configured auto booking (${interval}, ${this.cs.convertBack(amount)}€) for credit with ID ${credit.id}");
        val currentUser = this.securityService.getCurrentUser();
        this.mail.sendEntityActionMail(
            "Configured auto booking",
            "Configured auto booking (${interval}, ${this.cs.convertBack(amount)}€) for credit with ID ${credit.id}",
            currentUser?.email ?: "",
            MailEntityContext.credit,
            MailerSettingAction.UPDATE_ONLY,
            credit.isFavourite(currentUser)
        );
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
        this.denyUnlessGranted(CreditRateVoter.DELETE, obj);
        if (obj != null) {
            obj.credit.rates.remove(obj);
            this.entityManager.persist(obj.credit);
            this.delete(obj);
            this.entityManager.flush();
            this.log.writeLog("Deleted credit rate with ID $id");
            val currentUser = this.securityService.getCurrentUser();
            this.mail.sendEntityActionMail(
                "Deleted credit rate",
                "Deleted credit rate with ID ${id}",
                currentUser?.email ?: "",
                MailEntityContext.credit,
                MailerSettingAction.DELETE_ONLY,
                obj.credit.isFavourite(currentUser)
            );
        }
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
            if (!rate.archived) {
                sum += rate.amount!!;
                list.add(this.cs.convert(sum));
            }
        }
        return CreditResponse(this.convertCreditCurrencies(credit), this.cs.convert(sum), list, this.realEstateRepository.getByCredit(credit).id!!, credit.archived);
    }

    /**
     * Convert currencies of credit object.
     *
     * @param credit The initial credit
     * @return The updated credit
     */
    fun convertCreditCurrencies(credit: Credit): Credit {
        credit.amount = this.cs.convert(credit.amount!!);
        credit.rates = credit.rates.map { this.convertCreditRate(it) }.toMutableList();
        return credit;
    }

    /**
     * Convert currencies of credit rate
     *
     * @param rate The credit rate
     * @return The updated credit rate
     */
    private fun convertCreditRate(rate: CreditRate): CreditRate {
        rate.amount = this.cs.convert(rate.amount!!);
        return rate;
    }
}