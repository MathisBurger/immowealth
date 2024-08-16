package de.immowealth.service

import de.immowealth.entity.User
import de.immowealth.entity.enum.MailEntityContext
import de.immowealth.entity.enum.MailerSettingAction
import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty

/**
 * The mail service that handles mail sending
 */
@ApplicationScoped
class MailService {

    @Inject
    lateinit var mailer: Mailer;

    @Inject
    lateinit var settingsService: SettingsService;

    @ConfigProperty(name = "immowealth.defaultMail")
    lateinit var defaultMail: String


    /**
     * Sends a mail to a mail if everything is matching
     *
     * @param subject The subject
     * @param message The message
     * @param to The destination
     * @param context The sending context
     * @param action The required action
     * @param isFavourite Check if is Favourite
     */
    fun sendEntityActionMail(subject: String, message: String, to: String, context: MailEntityContext, action: MailerSettingAction, isFavourite: Boolean) {
        val setting = this.settingsService.getSetting("mailerNotification_${context.name}");
        val favouriteSetting = this.settingsService.getSetting("notificationsUseFavourites");
        if (action.name.equals(setting.value) || (setting.value ?: "").equals("ALL")) {
            if (favouriteSetting.value === "FAVOURITES") {
                if (isFavourite) {
                    this.mailer.send(
                        // TODO: Add HTML mail templates
                        Mail.withText(this.defaultMail, subject, message)
                    );
                }
            } else {
                this.mailer.send(
                    // TODO: Add HTML mail templates
                    Mail.withText(this.defaultMail, subject, message)
                );
            }
        }
    }

    /**
     * Sends an generic mail.
     *
     * @param subject The subject of the mail
     * @param message The message of the mail
     * @param to The target mail
     */
    fun sendMailHTML(subject: String, message: String, to: String) {
        this.mailer.send(
            Mail.withHtml(to, subject, message)
        );
    }
}