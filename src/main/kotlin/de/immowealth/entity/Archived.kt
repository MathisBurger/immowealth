package de.immowealth.entity

import jakarta.persistence.Column
import org.hibernate.annotations.ColumnDefault

/**
 * All entities that are archivable
 */
interface Archived {

    /**
     * The ID of the entity
     */
    var id: Long?;

    /**
     * If the entity is archived
     */
    var archived: Boolean
}