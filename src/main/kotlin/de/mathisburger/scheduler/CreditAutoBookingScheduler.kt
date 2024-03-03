package de.mathisburger.scheduler

import de.mathisburger.entity.CreditRate
import de.mathisburger.repository.CreditRepository
import de.mathisburger.util.AutoBookingUtils
import jakarta.enterprise.context.ApplicationScoped
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import java.util.Calendar

@ApplicationScoped
class CreditAutoBookingScheduler {

    @Inject
    lateinit var creditRepository: CreditRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    @Scheduled(cron = "0 0 1 * * ? *")
    @Transactional
    fun executeBookings() {
        val credits = this.creditRepository.findAllWithAutoBookingRequired();
        for (credit in credits) {
            val rate = CreditRate();
            rate.date = Calendar.getInstance().time;
            rate.amount = credit.autoPayAmount;
            this.entityManager.persist(rate);
            credit.nextCreditRate = AutoBookingUtils.getNextAutoPayIntervalDate(credit.autoPayInterval!!)
            credit.rates.add(rate);
            this.entityManager.persist(credit);
            this.creditRepository.flush();
        }
    }
}