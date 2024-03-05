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

@GraphQLApi
class RealEstateResource {

    @Inject
    lateinit var realEstateService: RealEstateService

    @Query
    fun getAllObjects(): List<RealEstateObject> {
        return this.realEstateService.getAllObjects();
    }

    @Query
    fun getObject(id: Long, yearsInFuture: Int?): ObjectResponse {
        val yif = yearsInFuture ?: 10;
        return this.realEstateService.getObject(id, yif);
    }

    @Mutation
    fun createRealEstate(input: RealEstateInput): RealEstateObject {
        return this.realEstateService.createObject(input);
    }

    @Mutation
    fun updateRealEstate(input: UpdateRealEstateInput): ObjectResponse {
        return this.realEstateService.updateObject(input);
    }
}