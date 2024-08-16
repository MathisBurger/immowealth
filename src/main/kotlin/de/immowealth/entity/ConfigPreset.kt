package de.immowealth.entity

import jakarta.persistence.Entity

/**
 * The config preset entity
 */
@Entity
class ConfigPreset : UserAuthorizedBaseEntity(), Archivable {

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