package de.mathisburger.entity

import jakarta.persistence.*
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.Date

/**
 * The real estate entity
 */
@Entity
class RealEstateObject : BaseEntity() {

    /**
     * The city of the object
     */
    var city: String? = null

    /**
     * The zip of the object
     */
    var zip: String? = null

    /**
     * The street and house nr
     */
    var streetAndHouseNr: String? = null

    /**
     * The initial value
     */
    var initialValue: Long? = null

    /**
     * The date bought
     */
    var dateBought: Date? = null

    /**
     * Position lat
     */
    var positionLat: Double? = null

    /**
     * Position lon
     */
    var positionLon: Double? = null

    /**
     * Amount of rooms
     */
    var rooms: Double? = null

    /**
     * Space for living
     */
    var space: Double? = null

    /**
     * Type of the object
     * e.g. house or flat (1. OG, EG)
     */
    var objectType: String? = null

    /**
     * Construction year
     */
    var constructionYear: Int? = null

    /**
     * Renovation year
     */
    var renovationYear: Int? = null

    /**
     * Energy efficiency class
     */
    var energyEfficiency: String? = null

    /**
     * Gross return of the object investment
     */
    var grossReturn: Double? = null

    /**
     * Garden exists
     */
    var garden: Boolean? = null

    /**
     * Kitchen exists
     */
    var kitchen: Boolean? = null

    /**
     * Heating type (wood, oil, gas)
     */
    var heatingType: String? = null

    /**
     * All multiline notes of the
     */
    var notes: String? = null;

    /**
     * The linked credit
     */
    @OneToOne(cascade = [CascadeType.ALL])
    var credit: Credit? = null

    /**
     * All rent expenses of the object
     */
    @OneToMany(mappedBy = "realEstateObject", cascade = [CascadeType.ALL])
    var expenses: MutableList<ObjectRentExpense> = mutableListOf();
}