package de.immowealth.entity

import de.immowealth.entity.enum.ObjectRentType
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

/**
 * A rent expense of an object
 */
@Entity
class ObjectRentExpense : BaseEntity(), Archivable {

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

    @ManyToOne
    lateinit var realEstateObject: RealEstateObject;
    override fun toString(): String {
        return this.name!!;
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String? {
        return null;
    }
}