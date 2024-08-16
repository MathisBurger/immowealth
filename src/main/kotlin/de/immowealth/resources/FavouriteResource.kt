package de.immowealth.resources

import de.immowealth.entity.Favourite
import de.immowealth.service.FavouriteService
import graphql.GraphQLError
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation

/**
 * GraphQL resource for favourites
 */
@GraphQLApi
class FavouriteResource {

    @Inject
    lateinit var favouriteService: FavouriteService

    @Mutation
    fun markAsFavourite(entityName: String, id: Long): Boolean {
        this.favouriteService.markAsFavourite(entityName, id);
        return true;
    }

    @Mutation
    fun unmarkAsFavourite(entityName: String, id: Long): Boolean {
        this.favouriteService.unmarkAsFavourite(entityName, id);
        return true;
    }
}