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
import java.util.Currency

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
            newLang.tab = "general";
            newLang.section = "general";
            newLang.options = mutableListOf(
                SettingOption("DE", "Deutsch", SettingOptionPrefix.deFlag),
                SettingOption("EN", "English", SettingOptionPrefix.enFlag)
            );
            newLang.value = "EN";
            this.entityManager.persist(newLang);
            this.entityManager.flush();
        }
        val currency = this.settingsRepository.getByKey("currency");
        if (currency.isEmpty) {
            var newCurrency = Setting()
            newCurrency.key = "currency";
            newCurrency.tab = "general";
            newCurrency.section = "general";
            newCurrency.options = mutableListOf(
                SettingOption("USD", "US Dollar", SettingOptionPrefix.USD),
                SettingOption("EUR", "Euro", SettingOptionPrefix.EUR),
                SettingOption("GBP", "Great Britan", SettingOptionPrefix.GBP),
                SettingOption("JPY", "Japan", SettingOptionPrefix.JPY),
                SettingOption("CHF", "Schweizer Franken", SettingOptionPrefix.CHF),
                SettingOption("CAD", "Canadian Dollar", SettingOptionPrefix.CAD),
                SettingOption("SEK", "Schwedische Krone", SettingOptionPrefix.SEK),
                SettingOption("NOK", "Norwegische Krone", SettingOptionPrefix.NOK),
                SettingOption("CNH", "China", SettingOptionPrefix.CNH),
                SettingOption("DKK", "Dänische Krone", SettingOptionPrefix.DKK),
                SettingOption("AED", "AED", SettingOptionPrefix.AED),
                SettingOption("RUB", "RUB", SettingOptionPrefix.RUB),
                SettingOption("MXN", "MXN", SettingOptionPrefix.MXN),
                SettingOption("TRY", "Türkei", SettingOptionPrefix.TRY)
            );
            newCurrency.value = "EUR";
            this.entityManager.persist(newCurrency);
            this.entityManager.flush();
        }

        this.initMailerSetting("realEstateObject");
        this.initMailerSetting("credit");
        this.initMailerSetting("housePrices");
    }

    private fun initMailerSetting(suffix: String) {
        val mailerNotification = this.settingsRepository.getByKey("mailerNotification_$suffix");
        if (mailerNotification.isEmpty) {
            val newMailerNotification = Setting();
            newMailerNotification.key = "mailerNotification_$suffix";
            newMailerNotification.tab = "notification";
            newMailerNotification.section = "mailer";
            newMailerNotification.options = mutableListOf(
                SettingOption("NONE", "None", null),
                SettingOption("UPDATE_ONLY", "Update only", null),
                SettingOption("CREATE_ONLY", "Create only", null),
                SettingOption("BOOKING_ONLY", "Booking only", null),
                SettingOption("DELETE_ONLY", "Delete only", null),
                SettingOption("ALL", "All", null)
            );
            newMailerNotification.value = "NONE";
            this.entityManager.persist(newMailerNotification);
            this.entityManager.flush();
        }
    }
}