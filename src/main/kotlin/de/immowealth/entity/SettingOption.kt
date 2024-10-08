package de.immowealth.entity

import de.immowealth.entity.enum.SettingOptionPrefix
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

/**
 * A setting option
 */
@Entity
class SettingOption : BaseEntity, Archivable {

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

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    var setting: Setting? = null;

    constructor() {
    }

    constructor(key: String, value: String, iconPrefix: SettingOptionPrefix?, translationKey: String?) {
        this.key = key;
        this.value = value;
        this.iconPrefix = iconPrefix;
        this.translationKey = translationKey;
    }

    override fun toString(): String {
        return this.key!!;
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String? {
        return null;
    }
}