package de.immowealth.service

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

    /**
     * Deletes an entity that implements archival features
     *
     * @param entity The entity that should be deleted
     */
    fun <T: BaseEntity> delete(entity: T) {
        if (entity.archived) {
            this.entityManager.remove(entity);
        } else {
            entity.archived = true;
            this.entityManager.persist(entity);
        }
    }
}