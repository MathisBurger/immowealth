package de.immowealth.service

import de.immowealth.entity.AuthorizedBaseEntity
import de.immowealth.entity.BaseEntity
import de.immowealth.entity.Favourite
import de.immowealth.entity.RealEstateObject
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class FavouriteService : AbstractService() {

    /**
     * Marks an entity instance as favourite for user
     *
     * @param entityName The name of the entity
     * @param id The ID of the entity
     */
    @Transactional
    fun markAsFavourite(entityName: String, id: Long) {
        val obj = this.entityManager.find(Class.forName(entityName), id);
        if (obj !== null && obj is AuthorizedBaseEntity && obj is Favourite) {
            this.denyUnlessGranted("READ", obj);
            val currentUser = this.securityService.getCurrentUser();
            if (!obj.isFavourite(currentUser) && currentUser !== null) {
                obj.favourite.add(currentUser);
                this.entityManager.persist(obj);
                this.entityManager.flush();
            }
        }
    }

    /**
     * Unmarks an entity instance as favourite
     *
     * @param entityName The name of the entity
     * @param id The ID of the entity instance
     */
    @Transactional
    fun unmarkAsFavourite(entityName: String, id: Long) {
        val obj = this.entityManager.find(Class.forName(entityName), id);
        if (obj !== null && obj is AuthorizedBaseEntity && obj is Favourite) {
            this.denyUnlessGranted("READ", obj);
            val currentUser = this.securityService.getCurrentUser();
            if (obj.isFavourite(currentUser) && currentUser !== null) {
                obj.favourite.remove(currentUser);
                this.entityManager.persist(obj);
                this.entityManager.flush();
            }
        }
    }
}