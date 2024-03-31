package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.util.Date

/**
 * Credit rate entity
 */
@Entity
class CreditRate : BaseEntity() {

    /**
     * The date of booking
     */
    var date: Date? = null;

    /**
     * The amount of money
     */
    var amount: Double? = null;

    /**
     * The note of the credit rate
     */
    var note: String? = null;

    @ManyToOne
    lateinit var credit: Credit;
}