package de.mathisburger.service

import de.mathisburger.entity.Setting
import de.mathisburger.repository.SettingsRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.eclipse.microprofile.graphql.GraphQLException

/**
 * Service that handles all settings transactions
 */
@ApplicationScoped
class SettingsService {

    @Inject
    lateinit var settingsRepository: SettingsRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    /**
     * Gets all settings
     *
     * @return All settings
     */
    fun getAllSettings(): List<Setting> {
        return this.settingsRepository.listAll();
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
        return instance;
    }
}