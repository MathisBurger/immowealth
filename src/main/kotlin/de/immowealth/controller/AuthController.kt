package de.immowealth.controller

import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.jwt.JsonWebToken

@Path("/api/auth")
class AuthController {

    @Inject
    lateinit var jsonWebToken: JsonWebToken;

    @GET
    @Path("/checkAuth")
    fun getAuthorized(@Context securityContext: SecurityContext): String {
        return securityContext.userPrincipal.name;
    }
}