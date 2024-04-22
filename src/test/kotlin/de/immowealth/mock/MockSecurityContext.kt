package de.immowealth.mock

import io.quarkus.test.Mock
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.SecurityContext
import java.security.Principal

/**
 * Mocked security context
 */
@Mock
@ApplicationScoped
class MockSecurityContext : SecurityContext {

    var principalName: String? = null;

    override fun getUserPrincipal(): Principal? {
        if (principalName == null) {
            return null;
        }
        val principal = MockedPrincipal();
        principal.principalName = this.principalName ?: "";
        return principal
    }

    override fun isUserInRole(role: String?): Boolean {
        return false;
    }

    override fun isSecure(): Boolean {
        return true;
    }

    override fun getAuthenticationScheme(): String {
        return "";
    }
}