package de.immowealth.mock

import io.quarkus.test.Mock
import jakarta.annotation.Priority
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.jwt.JsonWebToken

/**
 * Mocked JWT
 */
@Mock
@ApplicationScoped
@Priority(100)
class MockWebToken : JsonWebToken {

    var internalName: String? = null;

    override fun getName(): String {
        return internalName ?: ""
    }

    override fun getClaimNames(): MutableSet<String> {
        return mutableSetOf("")
    }

    override fun <T : Any?> getClaim(claimName: String?): T? {
        return null
    }

}