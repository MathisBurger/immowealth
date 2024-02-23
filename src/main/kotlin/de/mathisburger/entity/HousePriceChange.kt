package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class HousePriceChange {

    @GeneratedValue
    @Id
    var id: Long? = null;

    var zip: String? = null;

    var year: Int? = null;

    var change: Double? = null;
}