package de.mathisburger.resources

import de.mathisburger.entity.LogEntry
import de.mathisburger.service.LogService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Query

/**
 * Log entry resource for graphQL
 */
@GraphQLApi
class LogResource {

    @Inject
    lateinit var logService: LogService;

    /**
     * Gets all log entries paged
     */
    @Query
    fun getLogEntries(pageSize: Int, pageNr: Int): List<LogEntry> {
        return this.logService.getLogEntries(pageSize, pageNr);
    }

    /**
     * Gets the amount of all log entries
     */
    @Query
    fun getLogEntriesCount(): Int {
        return this.logService.getLogEntryRowCount();
    }
}