package de.mathisburger.startup

import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import java.io.File

/**
 * Handles creation of storage locations
 */
@ApplicationScoped
class StorageLocationCreator {


    /**
     * Creates file upload location
     */
    @Startup
    fun createFileUploadLocation() {
        File("./uploads").mkdirs();
    }
}