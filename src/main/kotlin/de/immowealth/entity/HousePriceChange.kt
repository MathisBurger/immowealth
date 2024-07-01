package de.immowealth.entity

import jakarta.persistence.Entity

/**
 * The change of real estate prices
 * in a specific region as entity
 */
@Entity
class HousePriceChange : AuthorizedBaseEntity(), Archivable {

    /**
     * The zip
     */
    var zip: String? = null;

    /**
     * The year
     */
    var year: Int? = null;

    /**
     * The change
     */
    var change: Double? = null;

    override fun toString(): String {
        return this.zip + " - " + this.change + " - " + this.year;
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String? {
        return null;
    }
}