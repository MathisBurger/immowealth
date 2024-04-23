package de.immowealth.service

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.ws.rs.core.SecurityContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

/**
 * Tests the config service
 */
@QuarkusTest
class ConfigPresetServiceTest : AbstractServiceTest() {

    @Inject
    override lateinit var securityContext: SecurityContext;

    @Inject
    lateinit var configPresetService: ConfigPresetService;

    @Test
    fun testGetAllForConfigPresetsWithoutLogin() {
        val data = this.configPresetService.getAllConfigPresets();
        assertEquals(0, data.size)
    }

    @Test
    fun testCreateConfigPreset() {
        this.loginAsUser("admin");
        val data = this.configPresetService.createOrUpdateConfigPreset("/test", "key", "{}");
        assertEquals(data.pageRoute, "/test")
    }

    @Test
    fun testCreateWithoutLogin() {
        try {
            this.configPresetService.createOrUpdateConfigPreset("/test", "key", "{}");
            fail("Should not be able to create config preset without login")
        } catch (e: Throwable) {
            assertTrue(true);
        }
    }

    @Test
    fun testUpdateConfigPreset() {
        this.loginAsUser("admin");
        val updated = this.configPresetService.createOrUpdateConfigPreset("/test", "key", "{JSON}");
        assertEquals(updated.jsonString, "{JSON}");
    }

    @Test
    fun testGetConfigPresets() {
        this.loginAsUser("admin");
        val data = this.configPresetService.getAllConfigPresets();
        assertEquals(data.size, 1);
    }
}