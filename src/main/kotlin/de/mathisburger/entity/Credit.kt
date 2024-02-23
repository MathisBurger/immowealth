package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Credit {

    @GeneratedValue
    @Id
    var id: Long? = null;

    var interestRate: Double? = null;

    var redemptionRate: Double? = null;

    var bank: String? = null;

    var amount: Long? = null;

    @OneToMany
    var rates: MutableList<CreditRate> = mutableListOf();
}