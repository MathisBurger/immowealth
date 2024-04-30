package de.immowealth.service

import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

/**
 * Tests settings service
 */
@QuarkusTest
class SettingsServiceTest : AbstractServiceTest() {

    @Inject
    override lateinit var securityContext: JsonWebToken;

    @Inject
    lateinit var settingsService: SettingsService;

    @Test
    fun testGetAllSettings() {
        this.loginAsUser("admin");
        val settings = this.settingsService.getAllSettings();
        assertTrue(settings.size > 1);
    }

    @Test
    fun testGetAllSettingsWithoutLogin() {
        this.logout();
        val settings = this.settingsService.getAllSettings();
        assertEquals(settings.size, 0)
    }

    @Test
    fun testGetSetting() {
        this.loginAsUser("admin");
        val setting = this.settingsService.getSetting("language");
        assertNotNull(setting)
    }

    @Test
    fun testGetSettingWithoutLogin() {
        this.logout()
        try {
            this.settingsService.getSetting("language");
            fail<String>("Should not be able to");
        } catch (e: AssertionFailedError) {
            throw e;
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        }
    }

    @Test
    fun testUpdateSetting() {
        this.loginAsUser("admin");
        val setting = this.settingsService.updateSetting("language", "newVal");
        assertEquals(setting.value, "newVal");
    }

    @Test
    fun testUpdateSettingWithoutLogin() {
        this.logout()
        try {
            this.settingsService.updateSetting("language", "newValue");
            fail<String>("Should not be able to");
        } catch (e: AssertionFailedError) {
            throw e;
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        }
    }

}