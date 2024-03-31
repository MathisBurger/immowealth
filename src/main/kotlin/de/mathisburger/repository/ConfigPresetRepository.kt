package de.mathisburger.repository

import de.mathisburger.entity.ConfigPreset
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.Optional

/**
 * Handles all getter activities for config presets
 */
@ApplicationScoped
class ConfigPresetRepository : PanacheRepository<ConfigPreset> {

    /**
     * Gets config preset by pathname.
     *
     * @param pathname The pathname
     */
    fun getByPathname(pathname: String): Optional<ConfigPreset> {
        return find("pageRoute", pathname).firstResultOptional();
    }

    /**
     * Gets config presets by pathname.
     *
     * @param pathname The pathname
     */
    fun getAllByPathname(pathname: String): List<ConfigPreset> {
        return find("pageRoute", pathname).list();
    }

    /**
     * Gets config preset by pathname and key.
     *
     * @param pathname The pathname
     * @param key The key
     */
    fun getByPathnameAndKey(pathname: String, key: String): Optional<ConfigPreset> {
        return find("pageRoute = ?1 and key = ?2", pathname, key).firstResultOptional();
    }
}