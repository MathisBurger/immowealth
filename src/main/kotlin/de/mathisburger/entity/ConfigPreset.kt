package de.mathisburger.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

/**
 * The config preset entity
 */
@Entity
class ConfigPreset {

    /**
     * The ID
     */
    @GeneratedValue
    @Id
    var id: Long? = null;

    /**
     * The page route the config is located at
     */
    var pageRoute: String? = null;

    /**
     * The json config string for the page
     */
    var jsonString: String? = null;

    /**
     * Specific key for specific pages with multiple configs
     */
    var key: String? = null;
}