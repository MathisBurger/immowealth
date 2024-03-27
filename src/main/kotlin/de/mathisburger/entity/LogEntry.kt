package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.Date

/**
 * Entry of event log system
 */
@Entity
class LogEntry : BaseEntity() {

    /**
     * The message of action
     */
    var message: String? = null;

    /**
     * The date of execution
     */
    var date: Date? = null;
}