package de.immowealth.controller

import de.immowealth.data.input.UploadedResource
import de.immowealth.service.FileService
import jakarta.annotation.security.PermitAll
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm


/**
 * File upload controller
 */
@Path("/file")
@ApplicationScoped
class FileController {

    @Inject
    lateinit var fileService: FileService;

    /**
     * Uploads a file to a real estate object
     */
    @POST
    @Path("/realEstateObject")
    @PermitAll
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun uploadFileToRealEstateObject(@MultipartForm form: UploadedResource): Response {
        this.fileService.uploadFileToObject(form);
        return Response.ok().build();
    }

    /**
     * Gets a file by ID
     */
    @GET
    @PermitAll
    fun getFile(@QueryParam("id") id: Long): Response {
        val file = this.fileService.getFile(id);
        return file;
    }
}