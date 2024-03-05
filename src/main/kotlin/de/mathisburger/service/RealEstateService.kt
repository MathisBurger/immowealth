package de.mathisburger.service

import de.mathisburger.api.NominatimApi
import de.mathisburger.data.input.RealEstateInput
import de.mathisburger.data.input.UpdateRealEstateInput
import de.mathisburger.data.response.ObjectResponse
import de.mathisburger.data.response.PriceValueRelation
import de.mathisburger.entity.Credit
import de.mathisburger.entity.RealEstateObject
import de.mathisburger.repository.HousePriceChangeRepository
import de.mathisburger.repository.RealEstateRepository
import de.mathisburger.util.DateUtils
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.util.Calendar

@ApplicationScoped
class RealEstateService {

    @Inject
    lateinit var entityManager: EntityManager

    @Inject
    lateinit var realEstateRepository: RealEstateRepository

    @Inject
    lateinit var priceChangeRepository: HousePriceChangeRepository;

    @Inject
    @RestClient
    lateinit var geocodingApi: NominatimApi;

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

        val results = this.geocodingApi.getLocations(input.streetAndHouseNr + " " + input.zip + " " + input.city);
        if (results.isNotEmpty()) {
            obj.positionLat = results.first().lat.toDouble();
            obj.positionLon = results.first().lon.toDouble();
        }

        this.entityManager.persist(obj);
        this.entityManager.flush();
        return obj;
    }

    fun getAllObjects(): List<RealEstateObject> {
        return this.realEstateRepository.listAll()
    }

    fun getObject(id: Long, yearsInFuture: Int = 10): ObjectResponse {
        val obj =  this.realEstateRepository.findById(id);
        var creditRateSum = 0.0;
        var cum: MutableList<Double> = mutableListOf();
        for (rate in obj.credit!!.rates) {
            creditRateSum += rate.amount!!;
            cum.add(creditRateSum);
        }
        val priceChanges = this.getPriceChanges(obj);
        val marketValue = if(priceChanges.isEmpty()) obj.initialValue!! else priceChanges.last().value;
        return ObjectResponse(
            obj,
            creditRateSum,
            cum,
            priceChanges,
            this.getPriceForecast(obj, yearsInFuture),
            marketValue
        );
    }

    @Transactional
    fun updateObject(input: UpdateRealEstateInput): ObjectResponse {
        val obj = this.realEstateRepository.findById(input.id);
        obj.zip = input.zip ?: obj.zip;
        obj.dateBought = input.dateBought ?: obj.dateBought;
        obj.initialValue = input.initialValue ?: obj.initialValue;
        obj.city = input.city ?: obj.city;
        obj.streetAndHouseNr = input.streetAndHouseNr ?: obj.streetAndHouseNr;
        this.entityManager.persist(obj);
        this.entityManager.flush();
        return this.getObject(obj.id!!, 10);
    }

    private fun getPriceChanges(obj: RealEstateObject): List<PriceValueRelation> {
        val changes = this.priceChangeRepository.findByZip(obj.zip!!)
            .filter { it.year!! >= DateUtils.getYearFromDate(obj.dateBought!!) }
            .sortedBy { it.year }
        val data: MutableList<PriceValueRelation> = mutableListOf();
        data.add(PriceValueRelation(obj.initialValue!!, DateUtils.getYearFromDate(obj.dateBought!!)));
        var value = obj.initialValue!!;
        for (change in changes) {
            value = (value + value * (change.change!! / 100)).toLong();
            data.add(PriceValueRelation(value, change.year!!));
        }
        return data;
    }

    private fun getPriceForecast(obj: RealEstateObject, yearsInFuture: Int): List<PriceValueRelation> {
        val priceChanges = this.getPriceChanges(obj);
        var averageGrowth = 2.0;
        if (priceChanges.isEmpty()) {
            averageGrowth = 2.0;
        } else {
            val diffMoney = priceChanges.last().value - obj.initialValue!!;
            var diffYears = priceChanges.last().year - DateUtils.getYearFromDate(obj.dateBought!!);
            if (diffYears == 0) {
                diffYears = 1;
            }
            val averageMoneyPerYear = diffMoney / diffYears;
            averageGrowth = averageMoneyPerYear.toDouble() / obj.initialValue!!
        }
        var value = (if(priceChanges.isEmpty()) obj.initialValue!! else priceChanges.last().value).toDouble();
        val currentYear = if(priceChanges.isEmpty()) DateUtils.getYearFromDate(obj.dateBought!!) else priceChanges.last().year;
        var data: MutableList<PriceValueRelation> = mutableListOf()
        for (i in 1..yearsInFuture) {
            value *= (1 + averageGrowth);
            data.add(PriceValueRelation(value.toLong(), currentYear+i));
        }
        return data;
    }
}