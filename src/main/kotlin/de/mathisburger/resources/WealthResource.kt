package de.mathisburger.resources

import de.mathisburger.data.response.WealthResponse
import de.mathisburger.service.WealthService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.GraphQLException
import org.eclipse.microprofile.graphql.Query

/**
 * Wealth graphQL resource
 */
@GraphQLApi
class WealthResource {

    /**
     * Wealth service
     */
    @Inject
    lateinit var wealthService: WealthService;

    /**
     * Gets gross wealth situation with inflation
     */
    @Query
    fun getGrossWealthWithInflation(): WealthResponse {
        return this.wealthService.getGrossWealthWithInflation();
    }

    /**
     * Gets gross wealth situation without inflation
     */
    @Query
    fun getGrossWealthWithoutInflation(): WealthResponse {
        return this.wealthService.getGrossWealthWithoutInflation();
    }

    /**
     * Gets net wealth situation with inflation
     */
    @Query
    fun getNetWealthWithInflation(): WealthResponse {
        return this.wealthService.getNetWealthWithInflation();
    }

    /**
     * Gets net wealth situation without inflation
     */
    @Query
    fun getNetWealthWithoutInflation(): WealthResponse {
        return this.wealthService.getNetWealthWithoutInflation();
    }
}