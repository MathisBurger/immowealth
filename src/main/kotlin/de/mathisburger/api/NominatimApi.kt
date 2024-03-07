package de.mathisburger.api

import de.mathisburger.data.api.PositionInfo
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

/**
 * The geocoding API rest service
 */
@RegisterRestClient(configKey = "geocoding")
interface NominatimApi {

    /**
     * Gets all API results from the
     * openstreetview geocoding API for a search string
     *
     * @param search The main search string
     * @param format The data format
     * @param details Addr details
     */
    @GET
    @Path("/search")
    fun getLocations(@QueryParam("q") search: String, @QueryParam("format") format: String = "json", @QueryParam("addressdetails") details: String = "1"): Set<PositionInfo>;

}