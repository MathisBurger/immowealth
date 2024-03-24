package de.mathisburger.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
 * Abstract service that is used by all other services
 */
@ApplicationScoped
abstract class AbstractService {

    /**
     * Currency convert service
     */
    @Inject
    lateinit var cs: ForeignExchangeRateService;

    /**
     * Service used for logging activities
     */
    @Inject
    lateinit var log: LogService;
}