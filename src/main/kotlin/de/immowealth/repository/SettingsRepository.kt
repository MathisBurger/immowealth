package de.immowealth.repository

import de.immowealth.entity.Setting
import de.immowealth.entity.User
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
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
    /*fun getByKey(key: String): Optional<Setting> {
        return find("key", key).firstResultOptional();
    }*/

    /**
     * Finds a result by key
     *
     * @param key The key that should be searched for
     * @param user The user that owns the setting
     *
     * @return The setting with the key
     */
    fun getByKey(key: String, user: User): Setting? {
        val cb = this.entityManager.criteriaBuilder;
        val cq = cb.createQuery(Setting::class.java);
        val root = cq.from(Setting::class.java);
        val userJoin = root.join<Setting, User>("user");
        val criteria = cb.and(
            cb.equal(userJoin.get<Long>("id"), user.id),
            cb.equal(root.get<String>("key"), key)
        );
        cq.select(root).where(criteria)
        try {
            return this.entityManager.createQuery(cq).singleResult;
        } catch (e: NoResultException) {
            return null;
        }
    }
}