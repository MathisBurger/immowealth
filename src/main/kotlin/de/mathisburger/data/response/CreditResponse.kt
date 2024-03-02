package de.mathisburger.data.response

import de.mathisburger.entity.Credit
import jakarta.json.bind.annotation.JsonbCreator

data class CreditResponse @JsonbCreator constructor(
    val credit: Credit,
    val creditRateSum: Double
);
