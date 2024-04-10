package de.immowealth.data.input

import jakarta.ws.rs.FormParam
import org.jboss.resteasy.annotations.providers.multipart.PartType
import java.io.File
import jakarta.ws.rs.core.MediaType

/**
 * Upload able multipart resource
 */
class UploadedResource() {
    /**
     * The uploaded file
     */
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    var file: File? = null;

    /**
     * The name of the file
     */
    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    var fileName: String? = null;

    /**
     * The root of the file
     */
    @FormParam("fileRoot")
    @PartType(MediaType.TEXT_PLAIN)
    var fileRoot: String? = null;

    /**
     * The ID of the object
     */
    @FormParam("objectId")
    @PartType(MediaType.TEXT_PLAIN)
    var objectId: String? = null;
}