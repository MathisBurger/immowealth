package de.immowealth.entity

import jakarta.persistence.Entity
import java.util.Date

/**
 * Entry of event log system
 */
@Entity
class LogEntry : AuthorizedBaseEntity(), Archivable {

    /**
     * The message of action
     */
    var message: String? = null;

    /**
     * The date of execution
     */
    var date: Date? = null;
    override fun toString(): String {
        return this.message!!;
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String? {
        return null;
    }
}