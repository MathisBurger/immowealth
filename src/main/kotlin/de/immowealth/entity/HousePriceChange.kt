package de.immowealth.entity

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany

/**
 * The change of real estate prices
 * in a specific region as entity
 */
@Entity
class HousePriceChange : AuthorizedBaseEntity(), Archivable, Favourite {

    @ManyToMany
    @JoinTable(name= "housePriceChange_favourite_user",
        joinColumns = [JoinColumn(name = "change_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
    override var favourite: MutableList<User> = mutableListOf()

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

    override fun isFavourite(user: User?): Boolean {
        return this.favourite.contains(user);
    }
}