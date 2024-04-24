package de.immowealth.service

import de.immowealth.data.input.UpdateHousePriceChangeInput
import de.immowealth.entity.HousePriceChange
import de.immowealth.entity.enum.MailEntityContext
import de.immowealth.entity.enum.MailerSettingAction
import de.immowealth.repository.HousePriceChangeRepository
import de.immowealth.voter.HousePriceChangeVoter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional

/**
 * The house price change service
 */
@ApplicationScoped
class HousePriceChangeService : AbstractService() {

    @Inject
    lateinit var housePriceChangeRepository: HousePriceChangeRepository;


    /**
     * Adds a new house price change
     *
     * @param zip The zip
     * @param cng The change
     * @param year The year
     */
    @Transactional
    fun addHousePriceChange(zip: String, cng: Double, year: Int): HousePriceChange {
        val change = HousePriceChange();
        change.change = cng;
        change.zip = zip;
        change.year = year;
        change.tenant = this.securityService.getCurrentUser()?.tenant;
        this.denyUnlessGranted(HousePriceChangeVoter.CREATE, change);
        this.entityManager.persist(change);
        this.entityManager.flush();
        this.log.writeLog("Added house price change at ($zip, $year) with $cng%");
        this.mail.sendEntityActionMail(
            "Added house price change",
            "Added house price change at ($zip, $year) with $cng%",
            "kontakt@mathis-burger.de",
            MailEntityContext.housePrices,
            MailerSettingAction.CREATE_ONLY
        );
        return change;
    }

    /**
     * Deletes a house price change
     *
     * @param id The ID
     */
    @Transactional
    fun delete(id: Long) {
        val obj = this.housePriceChangeRepository.findById(id);
        this.denyUnlessGranted(HousePriceChangeVoter.DELETE, obj);
        this.delete(obj);
        this.entityManager.flush();
        this.log.writeLog("Deleted house price change with ID $id");
        this.mail.sendEntityActionMail(
            "Deleted house price change",
            "Deleted house price change with ID $id",
            "kontakt@mathis-burger.de",
            MailEntityContext.housePrices,
            MailerSettingAction.DELETE_ONLY
        );
    }

    /**
     * Gets all changes
     */
    fun getAllChanges(): List<HousePriceChange> {
        return this.filterAccess(HousePriceChangeVoter.READ, this.housePriceChangeRepository.listAll());
    }

    /**
     * Gets all changes with zip
     *
     * @param zip The zip
     */
    fun getAllChangesWithZip(zip: String): List<HousePriceChange> {
        return this.filterAccess(HousePriceChangeVoter.READ, this.housePriceChangeRepository.findByZip(zip));
    }

    /**
     * Updates a house price
     *
     * @param input The update input
     */
    @Transactional
    fun updateHousePrices(input: UpdateHousePriceChangeInput): HousePriceChange {
        val obj = this.housePriceChangeRepository.findById(input.id);
        obj.change = input.change ?: obj.change;
        obj.zip = input.zip ?: obj.zip;
        obj.year = input.year ?: obj.year;
        this.denyUnlessGranted(HousePriceChangeVoter.UPDATE, obj);
        this.entityManager.persist(obj);
        this.entityManager.flush();
        this.log.writeLog("Updated house price change with id ${obj.id}");
        this.mail.sendEntityActionMail(
            "Updated house price change",
            "Updated house price change with id ${obj.id}",
            "kontakt@mathis-burger.de",
            MailEntityContext.housePrices,
            MailerSettingAction.UPDATE_ONLY
        );
        return obj;
    }
}