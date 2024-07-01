package de.immowealth.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import java.util.Date

/**
 * A renter of an object
 */
@Entity
class Renter : AuthorizedBaseEntity(), Archivable {

    /**
     * The object the renter is assigned to
     */
    @ManyToOne
    var realEstateObject: RealEstateObject? = null;

    /**
     * The first name of the renter
     */
    @Column(nullable = false)
    var firstName: String? = null

    /**
     * The last name of the renter
     */
    @Column(nullable = false)
    var lastName: String? = null

    /**
     * The birthday of the renter
     */
    @Column(nullable = true)
    var birthDay: Date? = null

    /**
     * User that is assigned to the renter
     */
    @OneToOne
    var user: User? = null

    override fun toString(): String {
        return this.id.toString();
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String? {
        return null;
    }
}