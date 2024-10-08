package de.immowealth.entity

import de.immowealth.entity.enum.AutoPayInterval
import jakarta.persistence.*
import java.util.Date

/**
 * The credit entity
 */
@Entity
class Credit : AuthorizedBaseEntity(), Archivable, Favourite {

    @ManyToMany
    @JoinTable(name= "credit_favourite_user",
        joinColumns = [JoinColumn(name = "credit_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
    override var favourite: MutableList<User> = mutableListOf()

    /**
     * The interest rate
     */
    var interestRate: Double? = null;

    /**
     * The redemption rate
     */
    var redemptionRate: Double? = null;

    /**
     * The bank
     */
    var bank: String? = null;

    /**
     * The amount of data
     */
    var amount: Long? = null;

    /**
     * The next credit rate
     */
    var nextCreditRate: Date? = null

    /**
     * Auto pay interval
     */
    var autoPayInterval: AutoPayInterval? = null

    /**
     * Auto pay amount
     */
    var autoPayAmount: Double? = null

    /**
     * All credit rates
     */
    @OneToMany(mappedBy = "credit", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var rates: MutableList<CreditRate> = mutableListOf();

    override fun toString(): String {
        return this.bank!! + " - " + this.amount;
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String {
        return "/credits/details?id=${this.id}";
    }

    override fun isFavourite(user: User?): Boolean {
        return this.favourite.contains(user);
    }
}