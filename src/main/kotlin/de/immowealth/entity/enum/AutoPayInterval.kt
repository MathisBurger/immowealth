package de.immowealth.entity.enum

/**
 * The auto pay interval of credit rates
 */
enum class AutoPayInterval(val value: String) {
    /**
     * Daily payments
     */
    DAILY("daily"),

    /**
     * Weekly payments
     */
    WEEKLY("weekly"),

    /**
     * Monthly payments
     */
    MONTHLY("monthly"),

    /**
     * Quarterly payments
     */
    QUARTERLY("quarterly"),

    /**
     * Half yearly payments
     */
    HALF_YEARLY("half_yearly"),

    /**
     * Yearly payments
     */
    YEARLY("yearly")
}