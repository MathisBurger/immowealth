package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

/**
 * Foreign exchange rate for currency calculations
 */
@Entity
class ForeignExchangeRate {

    /**
     * The ID
     */
    @GeneratedValue
    @Id
    var id: Long? = null;

    /**
     * The currency symbol
     */
    var symbol: String? = null;

    /**
     * The exchange rate
     */
    var rate: Double? = null;
}