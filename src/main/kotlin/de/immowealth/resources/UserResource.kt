package de.immowealth.resources

import de.immowealth.data.input.UserInput
import de.immowealth.entity.User
import de.immowealth.service.UserService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

/**
 * Handles user queries and mutations
 */
@GraphQLApi
class UserResource {

    @Inject
    lateinit var userService: UserService;

    /**
     * Gets the current user
     */
    @Query
    fun getCurrentUser(): User? {
        return this.userService.getCurrentUser();
    }

    /**
     * Gets all users
     */
    @Query
    fun getAllUsers(): List<User> {
        return this.userService.getAllUsers();
    }

    /**
     * Registers a new user
     */
    @Mutation
    fun registerUser(input: UserInput): User {
        return this.userService.registerUser(
            input.username,
            input.password,
            input.email,
            input.roles,
            input.tenantId
        )
    }
}