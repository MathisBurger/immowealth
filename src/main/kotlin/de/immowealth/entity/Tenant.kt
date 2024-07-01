package de.immowealth.entity

import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

/**
 * The tenant entity
 */
@Entity
class Tenant : BaseEntity(), Archivable {

    /**
     * The name of the tenant
     */
    var name: String? = null;

    /**
     * The users assigned to tenant
     */
    @OneToMany(mappedBy = "tenant")
    var users: MutableList<User> = mutableListOf();

    override fun toString(): String {
        return this.name ?: "";
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String? {
        return null
    }
}