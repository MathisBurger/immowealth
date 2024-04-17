package de.immowealth.controller

import de.immowealth.data.request.LoginRequest
import de.immowealth.service.SecurityService
import io.smallrye.jwt.auth.principal.JWTParser
import jakarta.annotation.security.PermitAll
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Cookie
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.jwt.JsonWebToken
import java.util.*

/**
 * Handles auth requests
 */
@Path("/api/auth")
class AuthController {

    @Inject
    lateinit var securityService: SecurityService;

    /**
     * Checks the auth status of the user
     */
    @GET
    @Path("/checkAuth")
    @PermitAll
    fun getAuthorized(@Context securityContext: SecurityContext): String {
        return securityContext.userPrincipal.name;
    }

    /**
     * Logs in the user
     */
    @POST
    @Path("/login")
    fun login(body: LoginRequest): Response {
        try {
            val res = this.securityService.login(body.username, body.password);
            val cookie = Cookie.Builder("jwt").value(res).path("/").build();
            val now = Calendar.getInstance();
            now.add(Calendar.HOUR, 1);
            val newCookie = NewCookie.Builder(cookie).expiry(now.time).build();
            return Response.ok().cookie(newCookie).build();
        } catch (e: NotAuthorizedException) {
            return Response.status(401).entity(e.message).build();
        }
    }
}