package de.immowealth.entity.enum

enum class MailerSettingAction(s: String) {

    ALL("ALL"),
    NONE("NONE"),
    UPDATE_ONLY("UPDATE_ONLY"),
    CREATE_ONLY("CREATE_ONLY"),
    BOOKING_ONLY("BOOKING_ONLY"),
    DELETE_ONLY("DELETE_ONLY")
}