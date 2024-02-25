package de.mathisburger.service

import de.mathisburger.entity.CreditRate
import de.mathisburger.repository.CreditRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import java.util.Date

@ApplicationScoped
class CreditService {

    @Inject
    lateinit var creditRepository: CreditRepository;

    @Inject
    lateinit var entityManager: EntityManager

    @Transactional
    fun addCreditRate(id: Long, rate: Double, date: Date) {
        val credit = this.creditRepository.findById(id);
        val creditRate = CreditRate()
        creditRate.date = date;
        creditRate.amount = rate;
        this.entityManager.persist(creditRate);
        credit.rates.add(creditRate);
        this.entityManager.persist(credit);
        this.entityManager.flush();
    }
}