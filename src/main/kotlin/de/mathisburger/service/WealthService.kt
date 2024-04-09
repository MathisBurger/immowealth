package de.mathisburger.service

import de.mathisburger.data.response.WealthResponse
import de.mathisburger.data.type.WealthSpreadType
import de.mathisburger.repository.HousePriceChangeRepository
import de.mathisburger.repository.RealEstateRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * The wealth service
 */
@ApplicationScoped
class WealthService : AbstractService() {

    @Inject
    lateinit var realEstateRepository: RealEstateRepository;

    @Inject
    lateinit var housePriceChangeRepository: HousePriceChangeRepository;

    /**
     * Gets gross wealth with inflation
     */
    fun getGrossWealthWithInflation(): WealthResponse {
        val objects = this.realEstateRepository.findAllArchived();
        var wealth = 0L;
        val detailed: MutableList<WealthSpreadType> = mutableListOf();
        for (obj in objects) {
            val priceChanges = this.housePriceChangeRepository.findByZip(obj.zip!!);
            var value = obj.initialValue!!;
            val calendar = GregorianCalendar();
            calendar.time = obj.dateBought!!;
            val startYear = calendar.get(Calendar.YEAR);
            for (priceChange in priceChanges) {
                if (priceChange.year!! > startYear) {
                    val multi = 1 + (priceChange.change!! / 100);
                    value = (value.toDouble() * multi).toLong();
                }
            }
            wealth += value;
            detailed.add(WealthSpreadType(obj.id!!, value, obj.streetAndHouseNr + ", " + obj.zip + " " + obj.city))
        }
        return this.conveertCurrencies(WealthResponse(wealth, detailed));
    }

    /**
     * Gets gross wealth without inflation
     */
    fun getGrossWealthWithoutInflation(): WealthResponse {
        val objects = this.realEstateRepository.findAllArchived();
        var wealth = 0L;
        val detailed: MutableList<WealthSpreadType> = mutableListOf();
        for (obj in objects) {
            wealth += obj.initialValue!!;
            detailed.add(WealthSpreadType(obj.id!!, obj.initialValue!!, obj.streetAndHouseNr + ", " + obj.zip + " " + obj.city))
        }
        return this.conveertCurrencies(WealthResponse(wealth, detailed));
    }

    /**
     * Gets net wealth with inflation
     */
    fun getNetWealthWithInflation(): WealthResponse {
        val objects = this.realEstateRepository.findAllArchived();
        var netWealth = 0L;
        val detailed: MutableList<WealthSpreadType> = mutableListOf()
        for (obj in objects) {
            var cumulatedRate = 0.0;
            for (rate in obj.credit!!.rates) {
                cumulatedRate += rate.amount!!;
            }
            if (cumulatedRate.toLong() < obj.credit!!.amount!!) {
                netWealth += cumulatedRate.toLong();
                detailed.add(WealthSpreadType(obj.id!!, cumulatedRate.toLong(), obj.streetAndHouseNr + ", " + obj.zip + " " + obj.city));
                continue;
            }
            val priceChanges = this.housePriceChangeRepository.findByZip(obj.zip!!);
            var value = obj.initialValue!!;
            val calendar = GregorianCalendar();
            calendar.time = obj.dateBought!!;
            val startYear = calendar.get(Calendar.YEAR);
            for (priceChange in priceChanges) {
                if (priceChange.year!! > startYear) {
                    val multi = 1 + (priceChange.change!! / 100);
                    value = (value.toDouble() * multi).toLong();
                }
            }
            netWealth += value;
            detailed.add(WealthSpreadType(obj.id!!, value, obj.streetAndHouseNr + ", " + obj.zip + " " + obj.city));
        }
        return this.conveertCurrencies(WealthResponse(netWealth, detailed));
    }

    /**
     * Gets net wealth without inflation
     */
    fun getNetWealthWithoutInflation(): WealthResponse {
        val objects = this.realEstateRepository.findAllArchived();
        var netWealth = 0L;
        val detailed: MutableList<WealthSpreadType> = mutableListOf()
        for (obj in objects) {
            var cumulatedRate = 0.0;
            for (rate in obj.credit!!.rates) {
                cumulatedRate += rate.amount!!;
            }
            if (cumulatedRate.toLong() < obj.credit!!.amount!!) {
                netWealth += cumulatedRate.toLong();
                detailed.add(WealthSpreadType(obj.id!!, cumulatedRate.toLong(), obj.streetAndHouseNr + ", " + obj.zip + " " + obj.city));
                continue;
            }
            netWealth += obj.initialValue!!;
            detailed.add(WealthSpreadType(obj.id!!, obj.initialValue!!, obj.streetAndHouseNr + ", " + obj.zip + " " + obj.city));
        }
        return this.conveertCurrencies(WealthResponse(netWealth, detailed));
    }

    /**
     * Converts currencies
     */
    private fun conveertCurrencies(resp: WealthResponse): WealthResponse {

        return WealthResponse(
            this.cs.convert(resp.total),
            resp.detailed.map { WealthSpreadType(it.id, this.cs.convert(it.value), it.label) }
        );
    }
}