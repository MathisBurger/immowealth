package de.mathisburger.repository

import de.mathisburger.entity.UploadedFile
import jakarta.enterprise.context.ApplicationScoped

/**
 * Repository for uploaded files
 */
@ApplicationScoped
class UploadedFileRepository : AbstractRepository<UploadedFile>() {
}