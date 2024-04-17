package de.immowealth.service

import de.immowealth.entity.Setting
import de.immowealth.repository.SettingsRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.graphql.GraphQLException

/**
 * Service that handles all settings transactions
 */
@ApplicationScoped
class SettingsService : AbstractService() {

    @Inject
    lateinit var settingsRepository: SettingsRepository;

    /**
     * Gets all settings
     *
     * @return All settings
     */
    fun getAllSettings(): List<Setting> {
        return this.settingsRepository.listAll();
    }

    /**
     * Gets a setting by key.
     *
     * @param key The key of the setting
     *
     * @return The setting
     */
    fun getSetting(key: String): Setting {
        return this.settingsRepository.getByKey(key).get();
    }

    /**
     * Updates a specific setting.
     *
     * @param key The key of the setting
     * @param value The new value of the setting
     *
     * @return The updated setting
     */
    @Transactional
    fun updateSetting(key: String, value: String): Setting {
        val setting = this.settingsRepository.getByKey(key);
        if (setting.isEmpty) {
            throw GraphQLException("Settings does not exist");
        }
        var instance = setting.get();
        instance.value = value;
        this.entityManager.persist(instance);
        this.entityManager.flush();
        this.log.writeLog("Updated setting ${setting.get().key} (ID: ${setting.get().id})");
        return instance;
    }
}