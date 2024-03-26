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
class ForeignExchangeRateService : AbstractService() {

    @Inject
    lateinit var foreignExchangeRateRepository: ForeignExchangeRateRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    @Inject
    @RestClient
    lateinit var restClient: ForeignExchangeRateApi;

    @Inject
    lateinit var settingsService: SettingsService;

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
        this.log.writeLog("Updated all foreign exchange rates");
    }

    /**
     * Converts the given value in EUR to the wanted currency.
     *
     * @param value The value as double
     * @return The converted value
     */
    fun convert(value: Double): Double {
        return value * this.getExchangeRate();
    }

    /**
     * Converts the given value in EUR to the wanted currency.
     *
     * @param value The value as int
     * @return The converted value
     */
    fun convert(value: Int): Int {
        return (value * this.getExchangeRate()).toInt();
    }

    /**
     * Converts the given value in EUR to the wanted currency.
     *
     * @param value The value as double
     * @return The converted value
     */
    fun convert(value: Long): Long {
        return (value * this.getExchangeRate()).toLong();
    }

    /**
     * Converts the given value from given currency to EUR.
     *
     * @param value The value as double
     * @return The converted value
     */
    fun convertBack(value: Double?): Double? {
        if (value == null) return null;
        return value / this.getExchangeRate();
    }

    /**
     * Converts the given value from given currency to EUR.
     *
     * @param value The value as int
     * @return The converted value
     */
    fun convertBack(value: Int?): Int? {
        if (value == null) return null;
        return (value / this.getExchangeRate()).toInt();
    }

    /**
     * Converts the given value from given currency to EUR.
     *
     * @param value The value as double
     * @return The converted value
     */
    fun convertBack(value: Long?): Long? {
        if (value == null) return null;
        return (value / this.getExchangeRate()).toLong();
    }

    /**
     * Gets the exchange rate that is required for calculation.
     *
     * @return The exchange rate
     */
    private fun getExchangeRate(): Double {
        val currency = this.settingsService.getSetting("currency").value;
        if (currency == "EUR") {
            return 1.0;
        }
        return this.foreignExchangeRateRepository.findBySymbol(currency!!).get().rate!!;
    }
}