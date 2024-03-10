package de.mathisburger.resources

import de.mathisburger.data.input.CreditRateInput
import de.mathisburger.data.input.UpdateCreditInput
import de.mathisburger.data.response.CreditResponse
import de.mathisburger.entity.enum.AutoPayInterval
import de.mathisburger.service.CreditService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

/**
 * Credit graphQL resource
 */
@GraphQLApi
class CreditResource {

    @Inject
    lateinit var creditService: CreditService;

    /**
     * Adda a new credit rate
     *
     * @param input The input
     */
    @Mutation
    fun addCreditRate(input: CreditRateInput): Boolean {
        this.creditService.addCreditRate(input.id, input.rate, input.date);
        return true;
    }

    /**
     * Configures credit auto pay
     *
     * @param id The credit ID
     * @param enabled If auto pay should be enabled
     * @param interval The pay interval
     * @param amount The pay amount
     */
    @Mutation
    fun configureCreditAutoPay(id: Long, enabled: Boolean, interval: AutoPayInterval?, amount: Double?): CreditResponse {
        return this.creditService.configureAutoBooking(id, enabled, interval, amount);
    }

    /**
     * Deletes a specific credit rate
     *
     * @param id The ID of the element that should be deleted
     */
    @Mutation
    fun deleteCreditRate(id: Long): Boolean {
        this.creditService.deleteCreditRate(id);
        return true;
    }

    /**
     * Gets all credits
     */
    @Query
    fun getAllCredits(): List<CreditResponse> {
        return this.creditService.getAllCredits();
    }

    /**
     * Gets a specific credit
     *
     * @param id The ID of the credit
     */
    @Query
    fun getCredit(id: Long): CreditResponse {
        return this.creditService.getCredit(id);
    }

    /**
     * Updates a credit
     */
    @Mutation
    fun updateCredit(input: UpdateCreditInput): CreditResponse {
        return this.creditService.updateCredit(input);
    }
}