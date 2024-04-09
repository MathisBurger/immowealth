package de.immowealth.startup

import de.immowealth.service.ForeignExchangeRateService
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
 * Handles startup actions of foreign exchange rates.
 */
@ApplicationScoped
class ForeignExchangeRateLoader {

    @Inject
    lateinit var foreignExchangeRateService: ForeignExchangeRateService;

    /**
     * Loads all exchange rates into the database on restart
     */
    @Startup
    fun load() {
        this.foreignExchangeRateService.reloadForeignExchangeRates();
    }

}