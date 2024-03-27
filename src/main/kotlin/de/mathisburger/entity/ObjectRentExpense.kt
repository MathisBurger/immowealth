package de.mathisburger.entity

import de.mathisburger.entity.enum.ObjectRentType
import jakarta.persistence.Entity

/**
 * A rent expense of an object
 */
@Entity
class ObjectRentExpense : BaseEntity() {

    /**
     * Name of the expense
     */
    var name: String? = null;

    /**
     * The type of the expense
     */
    var type: ObjectRentType? = null;

    /**
     * The monetary value of the expense
     */
    var value: Double? = null;
}