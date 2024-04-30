package de.immowealth.mock

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.ws.rs.core.SecurityContext
import org.junit.jupiter.api.Test

/**
 * tests all mock implementations
 */
@QuarkusTest
class TestMock {

    @Inject
    lateinit var securityContext: SecurityContext;

    @Test
    fun testInjection() {
        assert(this.securityContext is MockWebToken);
    }
}