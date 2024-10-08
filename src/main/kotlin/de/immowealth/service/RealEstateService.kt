package de.immowealth.service

import de.immowealth.api.NominatimApi
import de.immowealth.data.input.RealEstateInput
import de.immowealth.data.input.UpdateRealEstateInput
import de.immowealth.data.response.ObjectResponse
import de.immowealth.data.response.PriceValueRelation
import de.immowealth.entity.Chat
import de.immowealth.entity.Credit
import de.immowealth.entity.RealEstateObject
import de.immowealth.entity.UploadedFile
import de.immowealth.entity.enum.MailEntityContext
import de.immowealth.entity.enum.MailerSettingAction
import de.immowealth.repository.HousePriceChangeRepository
import de.immowealth.repository.RealEstateRepository
import de.immowealth.util.DateUtils
import de.immowealth.voter.RealEstateObjectVoter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.rest.client.inject.RestClient

/**
 * The real estate service
 */
@ApplicationScoped
class RealEstateService : AbstractService() {

    @Inject
    lateinit var realEstateRepository: RealEstateRepository

    @Inject
    lateinit var priceChangeRepository: HousePriceChangeRepository;

    @Inject
    lateinit var creditService: CreditService;

    @Inject
    @RestClient
    lateinit var geocodingApi: NominatimApi;

    /**
     * Creates a new real estate object
     *
     * @param input The data input
     */
    @Transactional
    fun createObject(input: RealEstateInput): RealEstateObject {
        val user = this.securityService.getCurrentUser();
        val credit = Credit();
        credit.bank = input.credit.bank;
        credit.interestRate = input.credit.interestRate;
        credit.redemptionRate = input.credit.redemptionRate;
        credit.tenant = user?.tenant;
        credit.amount = this.cs.convertBack(input.credit.amount);

        val obj = RealEstateObject()
        obj.city = input.city;
        obj.zip = input.zip;
        obj.streetAndHouseNr = input.streetAndHouseNr;
        obj.initialValue = this.cs.convertBack(input.initialValue);
        obj.credit = credit;
        obj.dateBought = input.dateBought;
        obj.rooms = input.rooms;
        obj.space = input.space;
        obj.objectType = input.objectType;
        obj.constructionYear = input.constructionYear;
        obj.renovationYear = input.renovationYear;
        obj.energyEfficiency = input.energyEfficiency;
        obj.grossReturn = input.grossReturn;
        obj.garden = input.garden;
        obj.kitchen = input.kitchen;
        obj.heatingType = input.heatingType;
        obj.notes = input.notes;
        obj.tenant = user?.tenant;

        this.denyUnlessGranted(RealEstateObjectVoter.CREATE, obj)
        this.entityManager.persist(credit);
        val results = this.geocodingApi.getLocations(input.streetAndHouseNr + " " + input.zip + " " + input.city);
        if (results.isNotEmpty()) {
            obj.positionLat = results.first().lat.toDouble();
            obj.positionLon = results.first().lon.toDouble();
        }
        this.entityManager.persist(obj);
        this.entityManager.flush();
        this.log.writeLog("Created real estate object (${obj.streetAndHouseNr}, ${obj.zip} ${obj.city})");
        val currentUser = this.securityService.getCurrentUser();
        this.mail.sendEntityActionMail(
            "Created real estate object",
            "Created real estate object (${obj.streetAndHouseNr}, ${obj.zip} ${obj.city})",
            currentUser?.email ?: "",
            MailEntityContext.realEstateObject,
            MailerSettingAction.CREATE_ONLY,
            obj.isFavourite(currentUser)
        );
        return obj;
    }

    /**
     * Gets all objects
     */
    fun getAllObjects(): List<RealEstateObject> {
        return this.filterAccess(RealEstateObjectVoter.READ, this.realEstateRepository.listAll()).map { this.convertObjectCurrencies(it) }
    }

    /**
     * Gets a specific object
     *
     * @param id The ID of the object
     * @param yearsInFuture The forecast years in future
     */
    fun getObject(id: Long, yearsInFuture: Int = 10): ObjectResponse {
        val obj =  this.realEstateRepository.findById(id);
        this.denyUnlessGranted(RealEstateObjectVoter.READ, obj);
        var creditRateSum = 0.0;
        val cum: MutableList<Double> = mutableListOf();
        for (rate in obj.credit!!.rates) {
            if (!rate.archived) {
                creditRateSum += rate.amount!!;
                cum.add(creditRateSum);
            }
        }
        val priceChanges = this.getPriceChanges(obj);
        val marketValue = if(priceChanges.isEmpty()) obj.initialValue!! else priceChanges.last().value;
        obj.uploadedFiles = obj.uploadedFiles.filter { !it.archived }.toMutableList();
        return ObjectResponse(
            this.convertObjectCurrencies(obj),
            this.cs.convert(creditRateSum),
            cum.map { this.cs.convert(it) },
            // Price changes is already converted
            priceChanges,
            // Price forecast is already converted
            this.getPriceForecast(obj, yearsInFuture),
            this.cs.convert(marketValue),
            obj.archived
        );
    }

    /**
     * Updates an object
     *
     * @param input The input for updating
     */
    @Transactional
    fun updateObject(input: UpdateRealEstateInput): ObjectResponse {
        val obj = this.realEstateRepository.findById(input.id);
        this.denyUnlessGranted(RealEstateObjectVoter.UPDATE, obj);
        obj.zip = input.zip ?: obj.zip;
        obj.dateBought = input.dateBought ?: obj.dateBought;
        obj.initialValue = this.cs.convertBack(input.initialValue) ?: obj.initialValue;
        obj.city = input.city ?: obj.city;
        obj.streetAndHouseNr = input.streetAndHouseNr ?: obj.streetAndHouseNr;
        obj.positionLat = input.positionLat ?: obj.positionLat;
        obj.positionLon = input.positionLon ?: obj.positionLon;
        obj.rooms = input.rooms ?: obj.rooms;
        obj.space = input.space ?: obj.space;
        obj.objectType = input.objectType ?: obj.objectType;
        obj.constructionYear = input.constructionYear ?: obj.constructionYear;
        obj.renovationYear = input.renovationYear ?: obj.renovationYear;
        obj.energyEfficiency = input.energyEfficiency ?: obj.energyEfficiency;
        obj.grossReturn = input.grossReturn ?: obj.grossReturn;
        obj.garden = input.garden ?: obj.garden;
        obj.kitchen = input.kitchen ?: obj.kitchen;
        obj.heatingType = input.heatingType ?: obj.heatingType;
        obj.notes = input.notes ?: obj.notes;

        this.entityManager.persist(obj);
        this.entityManager.flush();
        this.log.writeLog("Updated object with ID ${obj.id}");
        val currentUser = this.securityService.getCurrentUser();
        this.mail.sendEntityActionMail(
            "Updated real estate object",
            "Updated real estate object (${obj.streetAndHouseNr}, ${obj.zip} ${obj.city})",
            currentUser?.email ?: "",
            MailEntityContext.realEstateObject,
            MailerSettingAction.UPDATE_ONLY,
            obj.isFavourite(currentUser)
        );
        return this.getObject(obj.id!!, 10);
    }

    /**
     * Deletes an object
     *
     * @param The ID that should be deleted
     */
    @Transactional
    fun deleteObject(id: Long) {
        val obj = this.realEstateRepository.findById(id);
        this.denyUnlessGranted(RealEstateObjectVoter.DELETE, obj);
        this.delete(obj);
        this.entityManager.flush();
        this.log.writeLog("Deleted object with ID $id");
        val currentUser = this.securityService.getCurrentUser();
        this.mail.sendEntityActionMail(
            "Deleted real estate object",
            "Deleted real estate object (${obj.streetAndHouseNr}, ${obj.zip} ${obj.city})",
            currentUser?.email ?: "",
            MailEntityContext.realEstateObject,
            MailerSettingAction.DELETE_ONLY,
            obj.isFavourite(currentUser)
        );
    }

    /**
     * Gets all price changes of an object
     *
     * @param obj The object
     */
    private fun getPriceChanges(obj: RealEstateObject): List<PriceValueRelation> {
        val changes = this.priceChangeRepository.findByZip(obj.zip!!)
            .filter { it.year!! >= DateUtils.getYearFromDate(obj.dateBought!!) && !it.archived }
            .sortedBy { it.year }
        val data: MutableList<PriceValueRelation> = mutableListOf();
        data.add(PriceValueRelation(this.cs.convert(obj.initialValue!!), DateUtils.getYearFromDate(obj.dateBought!!)));
        var value = obj.initialValue!!;
        for (change in changes) {
            value = (value + value * (change.change!! / 100)).toLong();
            data.add(PriceValueRelation(this.cs.convert(value), change.year!!));
        }
        return data;
    }

    /**
     * Gets the price forecast of an object
     *
     * @param obj The object
     * @param yearsInFuture The years in future
     */
    private fun getPriceForecast(obj: RealEstateObject, yearsInFuture: Int): List<PriceValueRelation> {
        val priceChanges = this.getPriceChanges(obj);
        var averageGrowth = 2.0;
        if (priceChanges.isEmpty()) {
            averageGrowth = 2.0;
        } else {
            val diffMoney = priceChanges.last().value - this.cs.convert(obj.initialValue!!);
            var diffYears = priceChanges.last().year - DateUtils.getYearFromDate(obj.dateBought!!);
            if (diffYears == 0) {
                diffYears = 1;
            }
            val averageMoneyPerYear = diffMoney / diffYears;
            averageGrowth = averageMoneyPerYear.toDouble() / this.cs.convert(obj.initialValue!!)
        }
        var value = (if(priceChanges.isEmpty()) this.cs.convert(obj.initialValue!!) else priceChanges.last().value).toDouble();
        val currentYear = if(priceChanges.isEmpty()) DateUtils.getYearFromDate(obj.dateBought!!) else priceChanges.last().year;
        var data: MutableList<PriceValueRelation> = mutableListOf()
        for (i in 1..yearsInFuture) {
            value *= (1 + averageGrowth);
            data.add(PriceValueRelation(value.toLong(), currentYear+i));
        }
        return data;
    }

    /**
     * Converts an object with currencies
     *
     * @param obj The initial object
     * @return The updated object
     */
    private fun convertObjectCurrencies(obj: RealEstateObject): RealEstateObject {
        obj.initialValue = this.cs.convert(obj.initialValue!!);
        obj.credit = this.creditService.convertCreditCurrencies(obj.credit!!);
        return obj;
    }
}