package de.mathisburger.entity

import de.mathisburger.entity.enum.SettingOptionPrefix
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

/**
 * A setting option
 */
@Entity
class SettingOption : BaseEntity {

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

    var translationKey: String? = null;

    constructor() {
    }

    constructor(key: String, value: String, iconPrefix: SettingOptionPrefix?, translationKey: String?) {
        this.key = key;
        this.value = value;
        this.iconPrefix = iconPrefix;
        this.translationKey = translationKey;
    }
}