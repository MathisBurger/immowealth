package de.immowealth.service

import de.immowealth.entity.Archivable
import de.immowealth.entity.Archived
import de.immowealth.entity.BaseEntity
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager

/**
 * Abstract service that is used by all other services
 */
@ApplicationScoped
abstract class AbstractService {

    /**
     * Currency convert service
     */
    @Inject
    lateinit var cs: ForeignExchangeRateService;

    /**
     * Service used for logging activities
     */
    @Inject
    lateinit var log: LogService;

    /**
     * Mail service
     */
    @Inject
    lateinit var mail: MailService;

    /**
     * Own entity manager
     */
    @Inject
    lateinit var entityManager: EntityManager;

    @Inject
    lateinit var securityService: SecurityService;

    /**
     * Denies unless granted
     */
    fun denyUnlessGranted(attribute: String, value: Archived? = null) {
        if (!this.securityService.isGranted(attribute, value)) {
            throw Exception("No access");
        }
    }

    /**
     * Filters the user access
     *
     * @param attribute The attribute that should be checked for
     * @param value The values that should be checked for
     */
    fun filterAccess(attribute: String, value: List<Archived>): List<Archived> {
        return value.filter { this.securityService.isGranted(attribute, it) }
    }

    /**
     * Filters the user access
     *
     * @param attribute The attribute that should be checked for
     * @param value The values that should be checked for
     */
    fun filterAccessArchivable(attribute: String, value: List<Archivable>): List<Archivable> {
        return value.filter { it is Archived && this.securityService.isGranted(attribute, it) }
    }

    /**
     * Deletes an entity that implements archival features
     *
     * @param entity The entity that should be deleted
     */
    fun <T: Archived> delete(entity: T) {
        if (entity.archived) {
            this.entityManager.remove(entity);
        } else {
            entity.archived = true;
            this.entityManager.persist(entity);
        }
    }
}