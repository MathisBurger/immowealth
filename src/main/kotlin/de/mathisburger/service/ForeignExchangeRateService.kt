package de.mathisburger.service

import de.mathisburger.api.ForeignExchangeRateApi
import de.mathisburger.entity.ForeignExchangeRate
import de.mathisburger.repository.ForeignExchangeRateRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.eclipse.microprofile.rest.client.inject.RestClient

/**
 * Foreign exchange rate service that handles all reloads and calculations
 */
@ApplicationScoped
class ForeignExchangeRateService {

    @Inject
    lateinit var foreignExchangeRateRepository: ForeignExchangeRateRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    @Inject
    @RestClient
    lateinit var restClient: ForeignExchangeRateApi;

    /**
     * Reloads all foreign exchange rates
     */
    @Transactional
    fun reloadForeignExchangeRates() {
        val rates = this.restClient.getAllExchangeRates().fixingRates;
        for (rate in rates) {
            var opt = this.foreignExchangeRateRepository.findBySymbol(rate.currency);
            if (opt.isEmpty) {
                var curr = ForeignExchangeRate();
                curr.symbol = rate.currency;
                curr.rate = rate.offerRate.replace(',', '.').toDouble();
                this.entityManager.persist(curr);
            } else {
                var curr = opt.get();
                curr.rate = rate.offerRate.replace(',', '.').toDouble();
                this.entityManager.persist(curr);
            }
        }
        this.entityManager.flush();
    }
}