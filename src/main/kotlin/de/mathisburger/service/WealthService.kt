package de.mathisburger.service

import de.mathisburger.repository.HousePriceChangeRepository
import de.mathisburger.repository.RealEstateRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class WealthService {

    @Inject
    lateinit var realEstateRepository: RealEstateRepository;

    @Inject
    lateinit var housePriceChangeRepository: HousePriceChangeRepository;

    fun getGrossWealth(): Long {
        val objects = this.realEstateRepository.listAll();
        var wealth = 0L;
        for (obj in objects) {
            wealth += obj.initialValue!!;
        }
        return wealth;
    }
}