package de.mathisburger.resources

import de.mathisburger.data.input.CreditRateInput
import de.mathisburger.service.CreditService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation

@GraphQLApi
class CreditResource {

    @Inject
    lateinit var creditService: CreditService;

    @Mutation
    fun addCreditRate(input: CreditRateInput): Boolean {
        this.creditService.addCreditRate(input.id, input.rate, input.date);
        return true;
    }
}