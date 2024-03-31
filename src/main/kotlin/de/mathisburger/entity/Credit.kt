package de.mathisburger.entity

import de.mathisburger.entity.enum.AutoPayInterval
import jakarta.persistence.*
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
    @OneToMany(mappedBy = "credit", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var rates: MutableList<CreditRate> = mutableListOf();
}