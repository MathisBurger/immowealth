package de.mathisburger.entity

import jakarta.persistence.*
import java.util.Date

/**
 * The real estate entity
 */
@Entity
class RealEstateObject {

    /**
     * The ID of the object
     */
    @GeneratedValue
    @Id
    var id: Long? = null

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
     * The linked credit
     */
    @OneToOne
    var credit: Credit? = null
}