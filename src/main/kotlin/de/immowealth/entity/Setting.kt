package de.immowealth.entity

import jakarta.persistence.*

/**
 * Simple server side setting that is configured through the web
 */
@Entity
class Setting : UserAuthorizedBaseEntity(), Archivable {

    /**
     * The key of the setting
     */
    var key: String? = null;

    /**
     * All options for the value
     */
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "setting")
    var options: MutableList<SettingOption> = mutableListOf();

    /**
     * The tab of the settings
     */
    var tab: String? = null;

    /**
     * The section of the setting
     */
    var section: String? = null;

    /**
     * The actual value of the setting
     */
    var value: String? = null;
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