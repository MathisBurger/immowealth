package de.immowealth.resources

import de.immowealth.entity.ConfigPreset
import de.immowealth.service.ConfigPresetService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

/**
 * Resource for config presets entity
 */
@GraphQLApi
class ConfigPresetResource {

    @Inject
    lateinit var configPresetService: ConfigPresetService;

    /**
     * Gets all config presets
     */
    @Query
    fun getAllConfigPresets(): List<ConfigPreset> {
        return this.configPresetService.getAllConfigPresets();
    }

    /**
     * Gets a selection of config presets
     */
    @Query
    fun getAllConfigPresetsForPathname(pathname: String): List<ConfigPreset> {
        return this.configPresetService.getAllForPathname(pathname);
    }

    /**
     * Creates or updates config preset.
     */
    @Mutation
    fun createOrUpdateConfigPreset(pathname: String, key: String?, jsonContent: String): ConfigPreset {
        return this.configPresetService.createOrUpdateConfigPreset(pathname, key, jsonContent);
    }
}