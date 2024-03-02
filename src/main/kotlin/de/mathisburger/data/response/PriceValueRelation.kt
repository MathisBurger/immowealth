package de.mathisburger.data.response

import jakarta.json.bind.annotation.JsonbCreator

data class PriceValueRelation @JsonbCreator constructor(
    val value: Long,
    val year: Int
);
