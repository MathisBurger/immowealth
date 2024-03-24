package de.mathisburger.entity

import de.mathisburger.entity.enum.SettingOptionPrefix
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

/**
 * A setting option
 */
@Entity
class SettingOption {

    /**
     * The ID
     */
    @GeneratedValue
    @Id
    var id: Long? = null;

    /**
     * The key of the setting
     */
    var key: String? = null;

    /**
     * The value of the key
     */
    var value: String? = null;

    /**
     * Icon as prefix to select option
     */
    var iconPrefix: SettingOptionPrefix? = null;

    constructor() {}

    constructor(key: String, value: String, iconPrefix: SettingOptionPrefix) {
        this.key = key;
        this.value = value;
        this.iconPrefix = iconPrefix;
    }
}