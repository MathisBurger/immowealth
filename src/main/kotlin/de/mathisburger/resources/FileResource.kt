package de.mathisburger.resources

import de.mathisburger.service.FileService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation

/**
 * GraphQL API for files
 */
@GraphQLApi
class FileResource {

    @Inject
    lateinit var fileService: FileService;

    /**
     * Deletes a file
     */
    @Mutation
    fun deleteFile(id: Long): Boolean {
        this.fileService.deleteFile(id);
        return true;
    }
}