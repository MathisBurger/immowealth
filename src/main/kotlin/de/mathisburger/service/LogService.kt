package de.mathisburger.service

import de.mathisburger.entity.LogEntry
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

/**
 * Service that is used for logging data
 */
@ApplicationScoped
class LogService {

    @Inject
    lateinit var entityManager: EntityManager;

    /**
     * Writes a log element
     */
    @Transactional
    fun writeLog(message: String) {
        val logElement = LogEntry(message);
        this.entityManager.persist(logElement);
        this.entityManager.flush();
    }
}