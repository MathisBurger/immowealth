package de.immowealth.repository

import de.immowealth.entity.UploadedFile
import jakarta.enterprise.context.ApplicationScoped

/**
 * Repository for uploaded files
 */
@ApplicationScoped
class UploadedFileRepository : AbstractRepository<UploadedFile>() {
}