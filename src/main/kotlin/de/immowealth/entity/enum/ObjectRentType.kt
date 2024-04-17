package de.immowealth.entity.enum

/**
 * All rent spread types a expense can be
 */
enum class ObjectRentType(val str: String) {
    /**
     * Side costs like heating, electricity, e.g.
     */
    NK("NK"),

    /**
     * Redemption rate for the credit
     */
    REDEMPTION_RATE("REDEMPTION_RATE"),

    /**
     * The interest rate for credit
     */
    INTEREST_RATE("INTEREST_RATE"),

    /**
     * Reserve rate for credit
     */
    RESERVE("RESERVE"),

    /**
     * Insurance for object
     */
    INSURANCE("INSURANCE"),

    /**
     * Other expenses
     */
    OTHER("OTHER")
}