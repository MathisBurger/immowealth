package de.mathisburger.resources

import de.mathisburger.data.input.RealEstateInput
import de.mathisburger.data.input.UpdateRealEstateInput
import de.mathisburger.data.response.ObjectResponse
import de.mathisburger.entity.RealEstateObject
import de.mathisburger.service.RealEstateService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

/**
 * Real estate graphQL resource
 */
@GraphQLApi
class RealEstateResource {

    @Inject
    lateinit var realEstateService: RealEstateService

    /**
     * Gets all real estate objects without extra data
     */
    @Query
    fun getAllObjects(): List<RealEstateObject> {
        return this.realEstateService.getAllObjects();
    }

    /**
     * Gets a specific real estate object with specific data given
     *
     * @param id The object ID
     * @param yearsInFuture The forecast years in future
     */
    @Query
    fun getObject(id: Long, yearsInFuture: Int?): ObjectResponse {
        val yif = yearsInFuture ?: 10;
        return this.realEstateService.getObject(id, yif);
    }

    /**
     * Creates a new real estate object
     *
     * @param input The data input
     */
    @Mutation
    fun createRealEstate(input: RealEstateInput): RealEstateObject {
        return this.realEstateService.createObject(input);
    }

    /**
     * Updates a real estate object
     *
     * @param input Updated input
     */
    @Mutation
    fun updateRealEstate(input: UpdateRealEstateInput): ObjectResponse {
        return this.realEstateService.updateObject(input);
    }

    /**
     * Deletes an real estate object
     *
     * @param id The ID of the element that should be deleted
     */
    @Mutation
    fun deleteRealEstate(id: Long): Boolean {
        this.realEstateService.deleteObject(id);
        return true;
    }
}