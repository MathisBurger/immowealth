package de.mathisburger.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
abstract class AbstractService {

    @Inject
    lateinit var cs: ForeignExchangeRateService;
}