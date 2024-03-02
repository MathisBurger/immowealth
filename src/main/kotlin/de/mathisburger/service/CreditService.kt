package de.mathisburger.service

import de.mathisburger.data.response.CreditResponse
import de.mathisburger.entity.Credit
import de.mathisburger.entity.CreditRate
import de.mathisburger.exception.DateNotAllowedException
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

    fun getAllCredits(): List<CreditResponse> {
        val credits = this.creditRepository.listAll();
        val transform: (Credit) -> CreditResponse = {this.getResponseObject(it)};
        return credits.map(transform);
    }

    private fun getResponseObject(credit: Credit): CreditResponse {
        var sum = 0.0;
        for (rate in credit.rates) {
            sum += rate.amount!!;
        }
        return CreditResponse(credit, sum);
    }
}