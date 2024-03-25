package de.mathisburger.service

import de.mathisburger.entity.ConfigPreset
import de.mathisburger.repository.ConfigPresetRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

/**
 * Service that handles all config preset activities
 */
@ApplicationScoped
class ConfigPresetService : AbstractService() {

    @Inject
    lateinit var configPresetRepository: ConfigPresetRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    /**
     * Gets all config presets
     */
    fun getAllConfigPresets(): List<ConfigPreset> {
        return this.configPresetRepository.listAll();
    }

    /**
     * Creates or updates a config preset
     *
     * @param pathname The pathname
     * @param key The key
     * @param jsonContent The json content that is stored
     */
    @Transactional
    fun createOrUpdateConfigPreset(pathname: String, key: String?, jsonContent: String): ConfigPreset {
        val preF = if(key == null) this.configPresetRepository.getByPathname(pathname) else this.configPresetRepository.getByPathnameAndKey(pathname, key);
        if (preF.isEmpty) {
            val preset = ConfigPreset()
            preset.pageRoute = pathname;
            preset.key = key;
            preset.jsonString = jsonContent;
            this.entityManager.persist(preset);
            this.entityManager.flush();
            return preset;
        }
        val preset = preF.get();
        preset.jsonString = jsonContent;
        this.entityManager.persist(preset);
        this.entityManager.flush();
        return preset;
    }
}