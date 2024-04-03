package de.mathisburger.entity

import jakarta.persistence.*
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

    @ManyToOne(fetch = FetchType.EAGER)
    lateinit var credit: Credit;
}