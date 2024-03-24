package de.mathisburger.startup

import de.mathisburger.entity.Setting
import de.mathisburger.entity.SettingOption
import de.mathisburger.entity.enum.SettingOptionPrefix
import de.mathisburger.repository.SettingsRepository
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

@ApplicationScoped
class SettingsLoader {

    @Inject
    lateinit var settingsRepository: SettingsRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    /**
     * Loads all default settings into the database if they do not exist
     */
    @Startup
    @Transactional
    fun loadSettings() {

        val lang = this.settingsRepository.getByKey("language");
        if (lang.isEmpty) {
            var newLang = Setting()
            newLang.key = "language";
            newLang.tab = "General";
            newLang.section = "General";
            newLang.options = mutableListOf(
                SettingOption("deu", "Deutsch", SettingOptionPrefix.deFlag),
                SettingOption("eng", "English", SettingOptionPrefix.enFlag)
            );
            newLang.value = "English";
            this.entityManager.persist(newLang);
            this.entityManager.flush();
        }
    }
}