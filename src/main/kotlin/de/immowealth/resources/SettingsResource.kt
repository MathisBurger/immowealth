package de.immowealth.resources

import de.immowealth.entity.Setting
import de.immowealth.service.SettingsService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

/**
 * The graphQL API for settings
 */
@GraphQLApi
class SettingsResource {

    @Inject
    lateinit var settingsService: SettingsService;

    /**
     * Gets all settings
     */
    @Query
    fun getAllSettings() : List<Setting> {
        return this.settingsService.getAllSettings();
    }

    /**
     * Updates a setting
     *
     * @param key The key
     * @param value The new value
     *
     * @return The setting
     */
    @Mutation
    fun updateSetting(key: String, value: String): Setting {
        return this.settingsService.updateSetting(key, value);
    }
}