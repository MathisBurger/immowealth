package de.mathisburger.resources

import de.mathisburger.data.input.RealEstateInput
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

    @Mutation
    fun createRealEstate(input: RealEstateInput): RealEstateObject {
        return this.realEstateService.createObject(input);
    }
}