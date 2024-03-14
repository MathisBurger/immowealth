package de.mathisburger.service

import de.mathisburger.data.input.UpdateHousePriceChangeInput
import de.mathisburger.entity.HousePriceChange
import de.mathisburger.repository.HousePriceChangeRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

/**
 * The house price change service
 */
@ApplicationScoped
class HousePriceChangeService {

    @Inject
    lateinit var entityManager: EntityManager;

    @Inject
    lateinit var housePriceChangeRepository: HousePriceChangeRepository;


    /**
     * Adds a new house price change
     *
     * @param zip The zip
     * @param cng The change
     * @param year The year
     */
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

    /**
     * Deletes a house price change
     *
     * @param id The ID
     */
    @Transactional
    fun delete(id: Long) {
        val obj = this.housePriceChangeRepository.findById(id);
        this.entityManager.remove(obj);
        this.entityManager.flush();
    }

    /**
     * Gets all changes
     */
    fun getAllChanges(): List<HousePriceChange> {
        return this.housePriceChangeRepository.listAll();
    }

    /**
     * Gets all changes with zip
     *
     * @param zip The zip
     */
    fun getAllChangesWithZip(zip: String): List<HousePriceChange> {
        return this.housePriceChangeRepository.findByZip(zip);
    }

    /**
     * Updates a house price
     *
     * @param input The update input
     */
    @Transactional
    fun updateHousePrices(input: UpdateHousePriceChangeInput): HousePriceChange {
        val obj = this.housePriceChangeRepository.findById(input.id);
        obj.change = input.change ?: obj.change;
        obj.zip = input.zip ?: obj.zip;
        obj.year = input.year ?: obj.year;
        this.entityManager.persist(obj);
        this.entityManager.flush();
        return obj;
    }
}