package de.immowealth.data.response

import jakarta.json.bind.annotation.JsonbCreator

/**
 * The response of an archived entity
 */
data class ArchivedResponse @JsonbCreator constructor(
    /**
     * The value of the object
     */
    val value: String,
    /**
     * The entity name
     */
    val entityName: String,
    /**
     * The direct url to object
     */
    val directUrl: String?,
    /**
     * The ID of the entity
     */
    val entityID: Long
)