package de.mathisburger.service

import de.mathisburger.data.input.RealEstateInput
import de.mathisburger.entity.RealEstateObject
import de.mathisburger.repository.RealEstateRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

@ApplicationScoped
class RealEstateService {

    @Inject
    lateinit var entityManager: EntityManager

    @Inject
    lateinit var realEstateRepository: RealEstateRepository

    @Transactional
    fun createObject(input: RealEstateInput): RealEstateObject {
        val obj = RealEstateObject()
        obj.city = input.city;
        obj.zip = input.zip;
        obj.streetAndHouseNr = input.streetAndHouseNr;
        obj.initialValue = input.initialValue;
        this.entityManager.persist(obj);
        this.entityManager.flush();
        return obj;
    }

    fun getAllObjects(): List<RealEstateObject> {
        return this.realEstateRepository.listAll()
    }
}