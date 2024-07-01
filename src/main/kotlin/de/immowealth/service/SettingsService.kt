package de.immowealth.service

import de.immowealth.entity.Setting
import de.immowealth.repository.SettingsRepository
import de.immowealth.voter.SettingsVoter
import io.quarkus.security.UnauthorizedException
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
        return this.filterAccess(SettingsVoter.READ, this.settingsRepository.listAll());
    }

    /**
     * Gets a setting by key.
     *
     * @param key The key of the setting
     *
     * @return The setting
     */
    fun getSetting(key: String): Setting {
        val user = this.securityService.getCurrentUser() ?: throw UnauthorizedException();
        return this.settingsRepository.getByKey(key, user)!!;
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
        val user = this.securityService.getCurrentUser() ?: throw UnauthorizedException();
        val setting = this.settingsRepository.getByKey(key, user) ?: throw GraphQLException("Settings does not exist");
        setting.value = value;
        this.entityManager.persist(setting);
        this.entityManager.flush();
        this.log.writeLog("Updated setting ${setting.key} (ID: ${setting.id})");
        return setting;
    }
}