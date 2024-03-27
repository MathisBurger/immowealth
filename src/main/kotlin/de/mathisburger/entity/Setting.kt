package de.mathisburger.entity

import jakarta.persistence.*

/**
 * Simple server side setting that is configured through the web
 */
@Entity
class Setting : BaseEntity() {

    /**
     * The key of the setting
     */
    var key: String? = null;

    /**
     * All options for the value
     */
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.ALL], fetch = FetchType.EAGER)
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
}