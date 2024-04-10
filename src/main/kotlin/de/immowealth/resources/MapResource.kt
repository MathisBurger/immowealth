package de.immowealth.resources

import de.immowealth.data.response.MapResponse
import de.immowealth.repository.RealEstateRepository
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Query

@GraphQLApi
class MapResource {

    @Inject
    lateinit var realEstateRepository: RealEstateRepository;


    @Query
    fun getMapObjects(): MapResponse {
        val objs = this.realEstateRepository.listAll();
        var lonSum = 0.0;
        var latSum = 0.0;
        for (obj in objs) {
            lonSum += obj.positionLon!!;
            latSum += obj.positionLat!!;
        }
        return MapResponse(
            objs,
            latSum / objs.size,
            lonSum / objs.size
        );
    }
}