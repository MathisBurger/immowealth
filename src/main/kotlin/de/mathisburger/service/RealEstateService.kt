package de.mathisburger.service

import de.mathisburger.data.input.RealEstateInput
import de.mathisburger.entity.Credit
import de.mathisburger.entity.RealEstateObject
import de.mathisburger.repository.RealEstateRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import java.util.Date

@ApplicationScoped
class RealEstateService {

    @Inject
    lateinit var entityManager: EntityManager

    @Inject
    lateinit var realEstateRepository: RealEstateRepository

    @Transactional
    fun createObject(input: RealEstateInput): RealEstateObject {
        val credit = Credit();
        credit.bank = input.credit.bank;
        credit.interestRate = input.credit.interestRate;
        credit.redemptionRate = input.credit.redemptionRate;
        credit.amount = input.credit.amount;
        this.entityManager.persist(credit);

        val obj = RealEstateObject()
        obj.city = input.city;
        obj.zip = input.zip;
        obj.streetAndHouseNr = input.streetAndHouseNr;
        obj.initialValue = input.initialValue;
        obj.credit = credit;
        obj.dateBought = input.dateBought;
        this.entityManager.persist(obj);
        this.entityManager.flush();
        return obj;
    }

    fun getAllObjects(): List<RealEstateObject> {
        return this.realEstateRepository.listAll()
    }

    fun getObject(id: Long): RealEstateObject {
        return this.realEstateRepository.findById(id);
    }
}