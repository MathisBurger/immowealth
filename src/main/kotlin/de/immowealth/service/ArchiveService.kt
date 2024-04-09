package de.immowealth.service

import de.immowealth.entity.BaseEntity
import de.immowealth.entity.Credit
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ArchiveService : AbstractService() {

    /**
     * Removes archival flag from entity with ID
     *
     * @param id The ID of the entity
     * @param entityName The name of the entity
     */
    fun removeArchival(id: Long, entityName: String) {
        val cred = Credit()
        val obj = this.entityManager.find(Class.forName(entityName), id);
        if (obj != null && obj is BaseEntity) {
            obj.archived = false;
            this.entityManager.persist(obj);
            this.entityManager.flush();
        }
    }
}