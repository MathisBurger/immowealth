package de.mathisburger.api

import de.mathisburger.data.api.PositionInfo
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "geocoding")
interface NominatimApi {

    @GET
    @Path("/search")
    fun getLocations(@QueryParam("q") search: String, @QueryParam("format") format: String = "json", @QueryParam("addressdetails") details: String = "1"): Set<PositionInfo>;

}