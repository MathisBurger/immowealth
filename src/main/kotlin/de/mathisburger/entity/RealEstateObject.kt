package de.mathisburger.entity

import jakarta.persistence.*
import java.util.Date

@Entity
class RealEstateObject {

    @GeneratedValue
    @Id
    var id: Long? = null

    var city: String? = null

    var zip: String? = null

    var streetAndHouseNr: String? = null

    var initialValue: Long? = null

    var dateBought: Date? = null

    @OneToOne
    var credit: Credit? = null
}