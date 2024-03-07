package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.Date

/**
 * Credit rate entity
 */
@Entity
class CreditRate {

    /**
     * The ID
     */
    @GeneratedValue
    @Id
    var id: Long? = null;

    /**
     * The date of booking
     */
    var date: Date? = null;

    /**
     * The amount of money
     */
    var amount: Double? = null;
}