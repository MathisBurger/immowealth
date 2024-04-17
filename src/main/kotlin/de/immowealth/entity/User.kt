package de.immowealth.entity

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity

/**
 * The user entity
 */
@Entity(name = "immowealth_user")
class User : BaseEntity(), Archivable {

    /**
     * The username of the user
     */
    @Column(columnDefinition = "TEXT")
    var username: String = "";

    /**
     * The password of the user
     */
    var password: String = "";

    /**
     * All roles of the user
     */
    @ElementCollection
    var roles: MutableList<String> = mutableListOf();

    override fun toString(): String {
        return this.username;
    }

    override fun getEntityName(): String {
        return this.javaClass.toString();
    }

    override fun getDirectUrl(): String? {
        // TODO: Implement direct url to user
        return "";
    }
}