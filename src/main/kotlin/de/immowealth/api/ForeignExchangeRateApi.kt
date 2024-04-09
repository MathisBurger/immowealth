package de.immowealth.api

import de.immowealth.data.api.ExchangeResponse
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

/**
 * Foreign exchange rate api client provided by Commerzbank
 */
@RegisterRestClient(configKey = "foreignExchangeRate")
interface ForeignExchangeRateApi {

    /**
     * Gets all foreign exchange rates
     */
    @GET
    @Path("/efx-rates/payments/fixingrates/null/false")
    fun getAllExchangeRates(@QueryParam("lang") lang: String = "de"): ExchangeResponse;
}