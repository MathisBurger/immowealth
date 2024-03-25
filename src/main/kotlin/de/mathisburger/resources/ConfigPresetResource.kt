package de.mathisburger.resources

import de.mathisburger.entity.ConfigPreset
import de.mathisburger.service.ConfigPresetService
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
     * Creates or updates config preset.
     */
    @Mutation
    fun createOrUpdateConfigPreset(pathname: String, key: String?, jsonContent: String): ConfigPreset {
        return this.configPresetService.createOrUpdateConfigPreset(pathname, key, jsonContent);
    }
}