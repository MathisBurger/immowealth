package de.immowealth.resources

import de.immowealth.data.input.CreateRenterInput
import de.immowealth.entity.Renter
import de.immowealth.exception.ParameterException
import de.immowealth.service.RenterService
import graphql.GraphQLException
import io.quarkus.security.UnauthorizedException
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

/**
 * GraphQL resource for a renter
 */
@GraphQLApi
class RenterResource {

    @Inject
    lateinit var renterService: RenterService

    /**
     * Gets all unassigned renters
     */
    @Query
    fun getUnassignedRenters(): List<Renter> {
        return renterService.getUnassignedRenters();
    }

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

    /**
     * Deletes a renter from an object
     */
    @Mutation
    fun deleteRenterFromObject(renterId: Long): Boolean {
        try {
            this.renterService.deleteRenterFromObject(renterId);
            return true;
        } catch (e: UnauthorizedException) {
            throw GraphQLException(e.message);
        } catch (e: ParameterException) {
            throw GraphQLException(e.message);
        }
    }

    /**
     * Unassigns a renter from object
     */
    @Mutation
    fun unassignRenterFromObject(renterId: Long): Boolean {
        try {
            this.renterService.unassignRenterFromObject(renterId);
            return true;
        } catch (e: UnauthorizedException) {
            throw GraphQLException(e.message);
        } catch (e: ParameterException) {
            throw GraphQLException(e.message);
        }
    }

    /**
     * Assigns a renter to an object
     */
    @Mutation
    fun assignRenterToObject(renterId: Long, objectId: Long): Boolean {
        try {
            this.renterService.assignRenterToObject(renterId, objectId);
            return true;
        } catch (e: UnauthorizedException) {
            throw GraphQLException(e.message);
        } catch (e: ParameterException) {
            throw GraphQLException(e.message);
        }
    }
}