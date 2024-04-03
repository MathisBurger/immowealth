package de.mathisburger.service

import de.mathisburger.entity.LogEntry
import de.mathisburger.repository.LogEntryRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import java.util.*

/**
 * Service that is used for logging data
 */
@ApplicationScoped
class LogService {

    @Inject
    lateinit var entityManager: ExtendedEntityManager;

    @Inject
    lateinit var logEntryRepository: LogEntryRepository;

    /**
     * Pages all log entries by the given value
     *
     * @param pageSize The size of a page
     * @param pageNr The number of a page
     */
    fun getLogEntries(pageSize: Int, pageNr: Int): List<LogEntry> {
        return this.logEntryRepository.pageLogEntries(pageSize, pageNr);
    }

    /**
     * Gets the element count
     */
    fun getLogEntryRowCount(): Int {
        return this.logEntryRepository.getElementCount();
    }

    /**
     * Writes a log element
     */
    @Transactional
    fun writeLog(message: String) {
        val logElement = LogEntry();
        logElement.message = message;
        logElement.date = Date();
        this.entityManager.persist(logElement);
        this.entityManager.flush();
    }
}