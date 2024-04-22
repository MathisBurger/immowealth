package de.immowealth.startup

import de.immowealth.entity.Setting
import de.immowealth.entity.SettingOption
import de.immowealth.entity.User
import de.immowealth.entity.enum.MailEntityContext
import de.immowealth.entity.enum.MailerSettingAction
import de.immowealth.entity.enum.SettingOptionPrefix
import de.immowealth.repository.SettingsRepository
import de.immowealth.repository.UserRepository
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

    @Inject
    lateinit var userRepository: UserRepository;

    /**
     * Loads all default settings into the database if they do not exist
     */
    @Startup
    @Transactional
    fun loadSettings() {
        val users = this.userRepository.listAll();
        for (user in users) {
            val lang = this.settingsRepository.getByKey("language", user);
            if (lang === null) {
                val newLang = Setting()
                newLang.key = "language";
                newLang.tab = "general";
                newLang.user = user;
                newLang.section = "general";
                newLang.options = mutableListOf(
                    SettingOption("DE", "Deutsch", SettingOptionPrefix.deFlag, "lang.de"),
                    SettingOption("EN", "English", SettingOptionPrefix.enFlag, "lang.en")
                );
                newLang.value = "EN";
                this.entityManager.persist(newLang);
                this.entityManager.flush();
            }
            val currency = this.settingsRepository.getByKey("currency", user);
            if (currency === null) {
                val newCurrency = Setting()
                newCurrency.key = "currency";
                newCurrency.tab = "general";
                newCurrency.section = "general";
                newCurrency.user = user;
                newCurrency.options = mutableListOf(
                    SettingOption("USD", "US Dollar", SettingOptionPrefix.USD, null),
                    SettingOption("EUR", "Euro", SettingOptionPrefix.EUR, null),
                    SettingOption("GBP", "Great Britan", SettingOptionPrefix.GBP, null),
                    SettingOption("JPY", "Japan", SettingOptionPrefix.JPY, null),
                    SettingOption("CHF", "Schweizer Franken", SettingOptionPrefix.CHF, null),
                    SettingOption("CAD", "Canadian Dollar", SettingOptionPrefix.CAD, null),
                    SettingOption("SEK", "Schwedische Krone", SettingOptionPrefix.SEK, null),
                    SettingOption("NOK", "Norwegische Krone", SettingOptionPrefix.NOK, null),
                    SettingOption("CNH", "China", SettingOptionPrefix.CNH, null),
                    SettingOption("DKK", "Dänische Krone", SettingOptionPrefix.DKK, null),
                    SettingOption("AED", "AED", SettingOptionPrefix.AED, null),
                    SettingOption("RUB", "RUB", SettingOptionPrefix.RUB, null),
                    SettingOption("MXN", "MXN", SettingOptionPrefix.MXN, null),
                    SettingOption("TRY", "Türkei", SettingOptionPrefix.TRY, null)
                );
                newCurrency.value = "EUR";
                this.entityManager.persist(newCurrency);
                this.entityManager.flush();
            }

            this.initMailerSetting(MailEntityContext.realEstateObject.name, user);
            this.initMailerSetting(MailEntityContext.credit.name, user);
            this.initMailerSetting(MailEntityContext.housePrices.name, user);
        }
    }

    private fun initMailerSetting(suffix: String, user: User) {
        val mailerNotification = this.settingsRepository.getByKey("mailerNotification_$suffix", user);
        if (mailerNotification === null) {
            val newMailerNotification = Setting();
            newMailerNotification.key = "mailerNotification_$suffix";
            newMailerNotification.tab = "notification";
            newMailerNotification.section = "mailer";
            newMailerNotification.user = user;
            newMailerNotification.options = mutableListOf(
                SettingOption(MailerSettingAction.NONE.name, "None", null, "notification.mailer.none"),
                SettingOption(MailerSettingAction.UPDATE_ONLY.name, "Update only", null, "notification.mailer.update_only"),
                SettingOption(MailerSettingAction.CREATE_ONLY.name, "Create only", null, "notification.mailer.create_only"),
                SettingOption(MailerSettingAction.BOOKING_ONLY.name, "Booking only", null, "notification.mailer.booking_only"),
                SettingOption(MailerSettingAction.DELETE_ONLY.name, "Delete only", null, "notification.mailer.delete_only"),
                SettingOption(MailerSettingAction.ALL.name, "All", null, "notification.mailer.all")
            );
            newMailerNotification.value = "NONE";
            this.entityManager.persist(newMailerNotification);
            this.entityManager.flush();
        }
    }
}