package de.immowealth.scheduler

import de.immowealth.service.ForeignExchangeRateService
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
 * Scheduler that updates foreign exchange rates
 */
@ApplicationScoped
class ForeignExchangeRateScheduler {

    @Inject
    lateinit var foreignExchangeRateService: ForeignExchangeRateService;

    /**
     * Updates foreign exchange rates at 9 am.
     */
    @Scheduled(cron = "0 0 9 * * ? *")
    fun load() {
        this.foreignExchangeRateService.reloadForeignExchangeRates();
    }
}