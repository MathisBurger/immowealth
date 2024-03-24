package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.Date

/**
 * Entry of event log system
 */
@Entity
class LogEntry {
    /**
     * The ID
     */
    @GeneratedValue
    @Id
    var id: Long? = null;

    /**
     * The message of action
     */
    var message: String? = null;

    /**
     * The date of execution
     */
    var date: Date? = null;

    constructor() {}

    constructor(message: String) {
        this.message = message;
        this.date = Date();
    }
}