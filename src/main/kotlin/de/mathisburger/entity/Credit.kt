package de.mathisburger.entity

import de.mathisburger.entity.enum.AutoPayInterval
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.util.Date

@Entity
class Credit {

    @GeneratedValue
    @Id
    var id: Long? = null;

    var interestRate: Double? = null;

    var redemptionRate: Double? = null;

    var bank: String? = null;

    var amount: Long? = null;

    var nextCreditRate: Date? = null

    var autoPayInterval: AutoPayInterval? = null

    var autoPayAmount: Double? = null

    @OneToMany
    var rates: MutableList<CreditRate> = mutableListOf();
}