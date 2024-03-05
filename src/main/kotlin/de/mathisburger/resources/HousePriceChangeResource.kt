package de.mathisburger.resources

import de.mathisburger.entity.HousePriceChange
import de.mathisburger.service.HousePriceChangeService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

@GraphQLApi
class HousePriceChangeResource {

    @Inject
    lateinit var housePriceChangeService: HousePriceChangeService;

    @Mutation
    fun addHousePriceChange(zip: String, change: Double, year: Int): HousePriceChange {
        return this.housePriceChangeService.addHousePriceChange(zip, change, year);
    }

    @Mutation
    fun deleteHousePriceChange(id: Long): Boolean {
        this.housePriceChangeService.delete(id);
        return true;
    }

    @Query
    fun getAllHousePricesChange(): List<HousePriceChange> {
        return this.housePriceChangeService.getAllChanges();
    }

    @Query
    fun getAllHousePricesChangeByZip(zip: String): List<HousePriceChange> {
        return this.housePriceChangeService.getAllChangesWithZip(zip);
    }
}