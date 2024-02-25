package de.mathisburger.resources

import de.mathisburger.service.WealthService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Query

@GraphQLApi
class WealthResource {

    @Inject
    lateinit var wealthService: WealthService;

    @Query
    fun getGrossWealthWithInflation(): Long {
        return this.wealthService.getGrossWealthWithInflation();
    }

    @Query
    fun getGrossWealthWithoutInflation(): Long {
        return this.wealthService.getGrossWealthWithoutInflation();
    }

    @Query
    fun getNetWealthWithInflation(): Long {
        return this.wealthService.getNetWealthWithInflation();
    }

    @Query
    fun getNetWealthWithoutInflation(): Long {
        return this.wealthService.getNetWealthWithoutInflation();
    }
}