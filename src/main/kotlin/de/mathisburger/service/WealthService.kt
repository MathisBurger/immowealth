package de.mathisburger.service

import de.mathisburger.repository.HousePriceChangeRepository
import de.mathisburger.repository.RealEstateRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.Calendar
import java.util.GregorianCalendar

@ApplicationScoped
class WealthService {

    @Inject
    lateinit var realEstateRepository: RealEstateRepository;

    @Inject
    lateinit var housePriceChangeRepository: HousePriceChangeRepository;

    fun getGrossWealthWithInflation(): Long {
        val objects = this.realEstateRepository.listAll();
        var wealth = 0L;
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
        }
        return wealth;
    }

    fun getGrossWealthWithoutInflation(): Long {
        val objects = this.realEstateRepository.listAll();
        var wealth = 0L;
        for (obj in objects) {
            wealth += obj.initialValue!!;
        }
        return wealth;
    }

    fun getNetWealthWithInflation(): Long {
        val objects = this.realEstateRepository.listAll();
        var netWealth = 0L;
        for (obj in objects) {
            var cumulatedRate = 0.0;
            for (rate in obj.credit!!.rates) {
                cumulatedRate += rate.amount!!;
            }
            if (cumulatedRate.toLong() < obj.credit!!.amount!!) {
                netWealth += cumulatedRate.toLong();
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
        }
        return netWealth;
    }

    fun getNetWealthWithoutInflation(): Long {
        val objects = this.realEstateRepository.listAll();
        var netWealth = 0L;
        for (obj in objects) {
            var cumulatedRate = 0.0;
            for (rate in obj.credit!!.rates) {
                cumulatedRate += rate.amount!!;
            }
            if (cumulatedRate.toLong() < obj.credit!!.amount!!) {
                netWealth += cumulatedRate.toLong();
                continue;
            }
            netWealth += obj.initialValue!!;
        }
        return netWealth;
    }
}