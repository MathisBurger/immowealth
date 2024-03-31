package de.mathisburger.scheduler

import de.mathisburger.entity.CreditRate
import de.mathisburger.repository.CreditRepository
import de.mathisburger.service.LogService
import de.mathisburger.util.AutoBookingUtils
import de.mathisburger.util.DateUtils
import jakarta.enterprise.context.ApplicationScoped
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import java.util.Calendar

/**
 * Credit rate auto booking scheduler handler class
 */
@ApplicationScoped
class CreditAutoBookingScheduler {

    @Inject
    lateinit var creditRepository: CreditRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    @Inject
    lateinit var log: LogService;

    /**
     * Executes all bookings every day at 1am.
     */
    @Scheduled(cron = "0 0 1 * * ? *")
    @Transactional
    fun executeBookings() {
        val credits = this.creditRepository.findAllWithAutoBookingRequired();
        for (credit in credits) {
            val rate = CreditRate();
            rate.date = Calendar.getInstance().time;
            rate.amount = credit.autoPayAmount;
            rate.note = "[AUTOBOOKING] Buchung vom " + DateUtils.getTodayFormat();
            this.entityManager.persist(rate);
            credit.nextCreditRate = AutoBookingUtils.getNextAutoPayIntervalDate(credit.autoPayInterval!!)
            credit.rates.add(rate);
            this.entityManager.persist(credit);
            this.creditRepository.flush();
        }
        this.log.writeLog("Executed auto bookings of following credits ${credits.map { it.id }}");
    }
}