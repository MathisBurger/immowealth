package de.mathisburger.resources

import de.mathisburger.data.response.WealthResponse
import de.mathisburger.service.WealthService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Query

@GraphQLApi
class WealthResource {

    @Inject
    lateinit var wealthService: WealthService;

    @Query
    fun getGrossWealthWithInflation(): WealthResponse {
        return this.wealthService.getGrossWealthWithInflation();
    }

    @Query
    fun getGrossWealthWithoutInflation(): WealthResponse {
        return this.wealthService.getGrossWealthWithoutInflation();
    }

    @Query
    fun getNetWealthWithInflation(): WealthResponse {
        return this.wealthService.getNetWealthWithInflation();
    }

    @Query
    fun getNetWealthWithoutInflation(): WealthResponse {
        return this.wealthService.getNetWealthWithoutInflation();
    }
}