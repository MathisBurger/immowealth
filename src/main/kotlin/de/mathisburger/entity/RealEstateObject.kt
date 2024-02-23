package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class RealEstateObject {

    @GeneratedValue
    @Id
    var id: Long? = null

    var city: String? = null

    var zip: String? = null

    var streetAndHouseNr: String? = null

    var initialValue: Long? = null

}