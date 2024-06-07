package de.immowealth.resources

import de.immowealth.data.input.CreateRenterInput
import de.immowealth.entity.Renter
import de.immowealth.exception.ParameterException
import de.immowealth.service.RenterService
import graphql.GraphQLError
import graphql.GraphQLException
import io.quarkus.security.UnauthorizedException
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation

/**
 * GraphQL resource for a renter
 */
@GraphQLApi
class RenterResource {

    @Inject
    lateinit var renterService: RenterService

    /**
     * Creates a renter on an object
     */
    @Mutation
    fun createRenterOnObject(input: CreateRenterInput): Renter {
        try {
            return this.renterService.createRenterOnObject(input);
        } catch (e: UnauthorizedException) {
            throw GraphQLException(e.message);
        } catch (e: ParameterException) {
            throw GraphQLException(e.message);
        }
    }
}