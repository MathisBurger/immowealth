package de.mathisburger.repository

import de.mathisburger.entity.Setting
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.Optional

/**
 * The settings repository
 */
@ApplicationScoped
class SettingsRepository : AbstractRepository<Setting>() {

    /**
     * Finds a result by key
     *
     * @param key The key that should be searched for
     *
     * @return The setting with the key
     */
    fun getByKey(key: String): Optional<Setting> {
        return find("key", key).firstResultOptional();
    }
}