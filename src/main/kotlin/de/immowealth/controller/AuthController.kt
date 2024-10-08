package de.immowealth.controller

import de.immowealth.data.request.LoginRequest
import de.immowealth.data.request.ResetPasswordRequest
import de.immowealth.data.request.SetPasswordRequest
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
    fun getAuthorized(@Context securityContext: SecurityContext): Response {
        if (securityContext.userPrincipal !== null) {
            return Response.ok().build();
        }
        return Response.status(401).build()
    }

    /**
     * Logs in the user
     */
    @POST
    @Path("/login")
    fun login(body: LoginRequest): Response {
        try {
            val res = this.securityService.login(body.username, body.password);
            val cookie = Cookie.Builder("jwt")
                .value(res)
                .path("/")
                .build();
            val newCookie = NewCookie.Builder(cookie)
                .maxAge(3600)
                .httpOnly(false)
                .secure(true)
                .sameSite(NewCookie.SameSite.STRICT)
                .build();
            return Response
                .ok()
                .cookie(newCookie)
                .build();
        } catch (e: NotAuthorizedException) {
            return Response.status(401).entity(e.message).build();
        } catch (e: Throwable) {
            return Response.status(401).entity("Internal server error").build();
        }
    }

    /**
     * Resets the password and sends the email with reset link
     */
    @POST
    @Path("/resetPassword")
    fun resetPassword(body: ResetPasswordRequest): Response {
        this.securityService.resetPassword(body.username);
        return Response.ok().build();
    }

    /**
     * Sets the new password of the user
     */
    @POST
    @Path("/setPassword")
    fun setPassword(body: SetPasswordRequest, @Context securityContext: SecurityContext): Response {
        if (securityContext.userPrincipal === null) {
            return Response.status(401).build();
        }
        this.securityService.setNewPassword(securityContext.userPrincipal.name.toLong(), body.password);
        return Response.ok().build();
    }
}