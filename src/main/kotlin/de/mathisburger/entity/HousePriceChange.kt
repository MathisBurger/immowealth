package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

/**
 * The change of real estate prices
 * in a specific region as entity
 */
@Entity
class HousePriceChange {

    /**
     * The ID
     */
    @GeneratedValue
    @Id
    var id: Long? = null;

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
}