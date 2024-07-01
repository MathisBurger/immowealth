package de.immowealth.entity

import jakarta.persistence.*
import java.util.Date

/**
 * Credit rate entity
 */
@Entity
class CreditRate : AuthorizedBaseEntity(), Archivable {

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
    override fun toString(): String {
        return this.amount!!.toString() + " - " + this.credit.toString()
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String? {
        return null;
    }
}