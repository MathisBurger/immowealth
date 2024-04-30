package de.immowealth.resources

import de.immowealth.entity.User
import de.immowealth.service.UserService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Query

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
}