package de.immowealth.service

import de.immowealth.data.response.ArchivedResponse
import de.immowealth.entity.Archivable
import de.immowealth.entity.BaseEntity
import de.immowealth.entity.Credit
import de.immowealth.entity.UserRoles
import de.immowealth.repository.*
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.hibernate.type.EntityType

/**
 * Servive that handles archival actions
 */
@ApplicationScoped
class ArchiveService : AbstractService() {

    @Inject
    lateinit var configPresetRepository: ConfigPresetRepository;

    @Inject
    lateinit var creditRepository: CreditRepository;

    @Inject
    lateinit var creditRateRepository: CreditRateRepository;

    @Inject
    lateinit var foreignExchangeRateRepo: ForeignExchangeRateRepository;

    @Inject
    lateinit var housePriceChangeRepository: HousePriceChangeRepository;

    @Inject
    lateinit var logEntryRepository: LogEntryRepository;

    @Inject
    lateinit var objectRentExpenseRepository: ObjectRentExpenseRepository;

    @Inject
    lateinit var realEstateRepository: RealEstateRepository;

    @Inject
    lateinit var settingsRepository: SettingsRepository;

    @Inject
    lateinit var uploadedFileRepository: UploadedFileRepository;

    /**
     * Removes archival flag from entity with ID
     *
     * @param id The ID of the entity
     * @param entityName The name of the entity
     */
    @Transactional
    fun removeArchival(id: Long, entityName: String) {
        this.denyUnlessGranted(UserRoles.ADMIN);
        val obj = this.entityManager.find(Class.forName(entityName), id);
        if (obj != null && obj is BaseEntity) {
            obj.archived = false;
            this.entityManager.persist(obj);
            this.entityManager.flush();
        }
    }

    /**
     * Gets all archived entities
     */
    @Transactional
    fun getAllArchived(): List<ArchivedResponse> {
        val responses: MutableList<Archivable> = mutableListOf();
        responses.addAll(this.configPresetRepository.findAllThatAreArchived());
        responses.addAll(this.creditRepository.findAllThatAreArchived());
        //responses.addAll(this.creditRateRepository.findAllArchived());
        responses.addAll(this.foreignExchangeRateRepo.findAllThatAreArchived());
        responses.addAll(this.housePriceChangeRepository.findAllThatAreArchived());
        responses.addAll(this.logEntryRepository.findAllThatAreArchived());
        responses.addAll(this.objectRentExpenseRepository.findAllThatAreArchived());
        responses.addAll(this.realEstateRepository.findAllThatAreArchived());
        responses.addAll(this.settingsRepository.findAllThatAreArchived());
        responses.addAll(this.uploadedFileRepository.findAllThatAreArchived());
        return this.convertToResponse(this.filterAccessArchivable(UserRoles.ADMIN, responses));
    }

    /**
     * Converts all instances to responses
     */
    private fun convertToResponse(input: List<Archivable>): List<ArchivedResponse> {
        return input.map { ArchivedResponse(it.toString(), it.getEntityName().replace("class ", ""), it.getDirectUrl(), it.id!!) };
    }
}