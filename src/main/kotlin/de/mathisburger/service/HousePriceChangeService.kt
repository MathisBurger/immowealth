package de.mathisburger.service

import de.mathisburger.entity.HousePriceChange
import de.mathisburger.repository.HousePriceChangeRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

@ApplicationScoped
class HousePriceChangeService {

    @Inject
    lateinit var entityManager: EntityManager;

    @Inject
    lateinit var housePriceChangeRepository: HousePriceChangeRepository;


    @Transactional
    fun addHousePriceChange(zip: String, cng: Double, year: Int): HousePriceChange {
        val change = HousePriceChange();
        change.change = cng;
        change.zip = zip;
        change.year = year;
        this.entityManager.persist(change);
        this.entityManager.flush();
        return change;
    }

    fun getAllChanges(): List<HousePriceChange> {
        return this.housePriceChangeRepository.listAll();
    }

    fun getAllChangesWithZip(zip: String): List<HousePriceChange> {
        return this.housePriceChangeRepository.findByZip(zip);
    }
}