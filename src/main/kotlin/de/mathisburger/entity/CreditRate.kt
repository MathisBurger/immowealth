package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.Date

@Entity
class CreditRate {

    @GeneratedValue
    @Id
    var id: Long? = null;

    var date: Date? = null;

    var amount: Double? = null;
}