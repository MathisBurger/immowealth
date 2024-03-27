package de.mathisburger.entity

import de.mathisburger.entity.enum.AutoPayInterval
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import java.util.Date

/**
 * The credit entity
 */
@Entity
class Credit : BaseEntity() {


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
    @OneToMany
    var rates: MutableList<CreditRate> = mutableListOf();
}