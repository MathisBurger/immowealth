package de.mathisburger.repository

import de.mathisburger.entity.LogEntry
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.graphql.GraphQLException

/**
 * Log entry repository that handles all database actions
 * with log entries
 */
@ApplicationScoped
class LogEntryRepository : PanacheRepository<LogEntry> {

    /**
     * Pages all log entries by the given value
     *
     * @param pageSize The size of a page
     * @param pageNr The number of a page
     */
    fun pageLogEntries(pageSize: Int, pageNr: Int): List<LogEntry> {
        val fd = find("").page<LogEntry>(Page.ofSize(pageSize));
        if (fd.pageCount() < pageNr) {
            throw GraphQLException("Invalid pageNr");
        }
        return find("", Sort.by("date", Sort.Direction.Descending)).page<LogEntry>(Page.of(pageNr, pageSize)).list();
    }

    /**
     * Gets the element count
     */
    fun getElementCount(): Int {
        return listAll().count();
    }
}