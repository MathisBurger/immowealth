package de.immowealth.entity

import jakarta.persistence.Entity

/**
 * Foreign exchange rate for currency calculations
 */
@Entity
class ForeignExchangeRate : BaseEntity(), Archivable {

    /**
     * The currency symbol
     */
    var symbol: String? = null;

    /**
     * The exchange rate
     */
    var rate: Double? = null;

    override fun toString(): String {
        return this.symbol.toString();
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String? {
        return null;
    }
}