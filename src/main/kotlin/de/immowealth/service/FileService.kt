package de.immowealth.service

import de.immowealth.data.input.UploadedResource
import de.immowealth.entity.UploadedFile
import de.immowealth.repository.RealEstateRepository
import de.immowealth.repository.UploadedFileRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.Response
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import kotlin.io.path.Path


/**
 * Service that handles all file transactions
 */
@ApplicationScoped
class FileService : AbstractService() {

    @Inject
    lateinit var realEstateRepository: RealEstateRepository;

    @Inject
    lateinit var uploadedFileRepository: UploadedFileRepository;

    /**
     * Gets the file by ID
     *
     * @param id The ID of the file
     */
    fun getFile(id: Long): Response {
        val obj = this.uploadedFileRepository.findById(id);
        var file = File(obj.realFilePath!!);
        return Response.ok(file)
            .header("Content-Disposition", "attachment; filename=\"" + obj.fileName + "\"")
            .build();
    }

    /**
     * Uploads a file and appends it to a real estate object
     *
     * @param id The ID of the real estate object
     * @param resource The multipart resource
     */
    @Transactional
    fun uploadFileToObject(resource: UploadedResource) {
        val obj = this.realEstateRepository.findById(resource.objectId?.toLong() ?: -1);
        val file = UploadedFile()
        file.fileName = resource.fileName;
        file.fileRoot = resource.fileRoot;
        file.realEstateObject = obj;
        val newPath = this.getNewFilePath(resource.file!!, resource.fileName!!);
        newPath.toFile().createNewFile();
        file.realFilePath = newPath.toFile().absolutePath;
        Files.copy(resource.file!!.toPath(),newPath, StandardCopyOption.REPLACE_EXISTING);
        this.entityManager.persist(file);
        obj.uploadedFiles.add(file);
        this.entityManager.persist(obj);
        this.entityManager.flush();
    }

    /**
     * Deletes a file
     *
     * @param id The ID of the file
     */
    @Transactional
    fun deleteFile(id: Long) {
        val obj = this.uploadedFileRepository.findById(id);
        try {
            Files.delete(Path(obj.realFilePath!!));
        } catch (e: IOException) {
            // Do nothing
        }
        if (obj.realEstateObject != null) {
            obj.realEstateObject!!.uploadedFiles.remove(obj);
            this.entityManager.persist(obj.realEstateObject);
        }
        this.delete(obj);
        this.entityManager.flush();
    }

    /**
     * Gets the file path of a new file
     *
     * @param file The file
     */
    private fun getNewFilePath(file: File, name: String): Path {
        return Path("uploads/${this.createHashOfFile(file)}.${file.name}.$name");
    }

    /**
     * Creates the hash of a file
     *
     * @param file The file
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun createHashOfFile(file: File): String {
        val b = Files.readAllBytes(Paths.get(file.absolutePath));
        val hash = MessageDigest.getInstance("MD5").digest(b);
        return hash.toHexString();
    }
}