package de.mathisburger.service

import de.mathisburger.entity.enum.MailEntityContext
import de.mathisburger.entity.enum.MailerSettingAction
import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import io.smallrye.common.annotation.Blocking
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
     */
    fun sendEntityActionMail(subject: String, message: String, to: String, context: MailEntityContext, action: MailerSettingAction) {
        val setting = this.settingsService.getSetting("mailerNotification_${context.name}");
        if (action.name.equals(setting.value) || (setting.value ?: "").equals("ALL")) {
            this.mailer.send(
                // TODO: Add HTML mail templates
                Mail.withText(this.defaultMail, subject, message)
            );
        }
    }
}