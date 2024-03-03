package de.mathisburger.entity.enum

enum class AutoPayInterval(val value: String) {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    QUARTERLY("quarterly"),
    HALF_YEARLY("half_yearly"),
    YEARLY("yearly")
}