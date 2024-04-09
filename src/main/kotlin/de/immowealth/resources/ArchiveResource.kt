package de.immowealth.resources

import de.immowealth.service.ArchiveService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation

/**
 * Handles archiving
 */
@GraphQLApi
class ArchiveResource {

    @Inject
    lateinit var archiveService: ArchiveService;

    /**
     * Removes archivalStatus
     */
    @Mutation
    fun removeArchival(id: Long, entityName: String): Boolean {
        this.archiveService.removeArchival(id, entityName);
        return true;
    }
}