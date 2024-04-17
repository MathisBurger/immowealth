package de.immowealth.resources

import de.immowealth.data.response.ArchivedResponse
import de.immowealth.service.ArchiveService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

/**
 * Handles archiving
 */
@GraphQLApi
class ArchiveResource {

    @Inject
    lateinit var archiveService: ArchiveService;

    /**
     * Gets all archived elements
     */
    @Query
    fun getAllArchivedEntities(): List<ArchivedResponse> {
        return this.archiveService.getAllArchived();
    }

    /**
     * Removes archivalStatus
     */
    @Mutation
    fun removeArchival(id: Long, entityName: String): Boolean {
        this.archiveService.removeArchival(id, entityName);
        return true;
    }
}