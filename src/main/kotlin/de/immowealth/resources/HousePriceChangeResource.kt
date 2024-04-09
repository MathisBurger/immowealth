package de.immowealth.resources

import de.immowealth.data.input.UpdateHousePriceChangeInput
import de.immowealth.entity.HousePriceChange
import de.immowealth.service.HousePriceChangeService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

/**
 * House price change graphQL resource
 */
@GraphQLApi
class HousePriceChangeResource {

    @Inject
    lateinit var housePriceChangeService: HousePriceChangeService;

    /**
     * Adda a new house price change
     *
     * @param zip The zip of change
     * @param change The amount in percentage
     * @param year The year of change
     */
    @Mutation
    fun addHousePriceChange(zip: String, change: Double, year: Int): HousePriceChange {
        return this.housePriceChangeService.addHousePriceChange(zip, change, year);
    }

    /**
     * Deletes a specific house price change
     *
     * @param id The ID of the element that should be deleted
     */
    @Mutation
    fun deleteHousePriceChange(id: Long): Boolean {
        this.housePriceChangeService.delete(id);
        return true;
    }

    /**
     * Gets all house price changes
     */
    @Query
    fun getAllHousePricesChange(): List<HousePriceChange> {
        return this.housePriceChangeService.getAllChanges();
    }

    /**
     * Gets all house price changes with specific zip
     *
     * @param zip The zip
     */
    @Query
    fun getAllHousePricesChangeByZip(zip: String): List<HousePriceChange> {
        return this.housePriceChangeService.getAllChangesWithZip(zip);
    }

    /**
     * Updates the house price change
     */
    @Mutation
    fun updateHousePriceChange(input: UpdateHousePriceChangeInput): HousePriceChange {
        return this.housePriceChangeService.updateHousePrices(input);
    }
}