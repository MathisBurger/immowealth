package de.immowealth.service

import de.immowealth.data.input.CreditInput
import de.immowealth.data.input.RealEstateInput
import de.immowealth.data.input.UploadedResource
import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.SecurityContext
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.opentest4j.AssertionFailedError
import java.io.File
import java.util.*

/**
 * Tests the file service
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class FileServiceTest : AbstractServiceTest() {

    companion object {
        var objectId = -1L
    }

    @Inject
    override lateinit var securityContext: SecurityContext

    @Inject
    lateinit var fileService: FileService;

    @Inject
    lateinit var objectService: RealEstateService;

    @Inject
    lateinit var tenantService: TenantService;

    @Test
    @Order(1)
    fun testUploadFileToObject() {
        this.loginAsUser("admin")
        this.tenantService.createTenant("file_ten1", "file_tenUser", "123", "a@b.com");
        this.loginAsUser("file_tenUser")
        this.createObject();
        val resource = UploadedResource()
        resource.file = File.createTempFile("temp", ".pdf")
        resource.fileName = "Temp"
        resource.fileRoot = "data"
        resource.objectId = objectId.toString()
        this.fileService.uploadFileToObject(resource)
        val obj = this.objectService.getObject(objectId);
        assertEquals(obj.realEstate.uploadedFiles.size, 1)
    }

    @Test
    @Order(2)
    fun testUploadFileToObjectAsOtherUser() {
        this.loginAsUser("admin")
        this.tenantService.createTenant("file_ten2", "file_tenUser2", "123", "a@b.com");
        this.loginAsUser("file_tenUser2")
        val resource = UploadedResource()
        resource.file = File.createTempFile("temp2", ".pdf")
        resource.fileName = "Temp"
        resource.fileRoot = "data"
        resource.objectId = objectId.toString()
        try {
            this.fileService.uploadFileToObject(resource);
            fail<String>("Expected file not to be uploaded")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(3)
    fun testUploadFileToObjectWithoutLogin() {
        this.logout()
        val resource = UploadedResource()
        resource.file = File.createTempFile("temp3", ".pdf")
        resource.fileName = "Temp"
        resource.fileRoot = "data"
        resource.objectId = objectId.toString()
        try {
            this.fileService.uploadFileToObject(resource);
            fail<String>("Expected file not to be uploaded")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(4)
    fun testGetFile() {
        this.loginAsUser("file_tenUser");
        val obj = this.objectService.getObject(objectId);
        val wantedID = obj.realEstate.uploadedFiles.get(0).id
        val file = this.fileService.getFile(wantedID ?: -1L);
        assertEquals(file.status, 200);
    }

    @Test
    @Order(5)
    fun testGetFileAsOtherUser() {
        this.loginAsUser("file_tenUser");
        val obj = this.objectService.getObject(objectId);
        val wantedID = obj.realEstate.uploadedFiles.get(0).id
        this.loginAsUser("file_tenUser2");
        try {
            this.fileService.getFile(wantedID ?: -1L);
            fail<String>("Expected file not to be uploaded")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(6)
    fun testGetFileWithoutLogin() {
        this.loginAsUser("file_tenUser");
        val obj = this.objectService.getObject(objectId);
        val wantedID = obj.realEstate.uploadedFiles.get(0).id
        this.logout();
        try {
            this.fileService.getFile(wantedID ?: -1L);
            fail<String>("Expected file not to be uploaded")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(7)
    fun testDeleteFileWithoutLogin() {
        this.loginAsUser("file_tenUser");
        val obj = this.objectService.getObject(objectId);
        val wantedID = obj.realEstate.uploadedFiles.get(0).id;
        this.logout();
        try {
            this.fileService.deleteFile(wantedID ?: -1L);
            fail<String>("Expected file not to be deleted")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(8)
    fun testDeleteFileAsOtherUser() {
        this.loginAsUser("file_tenUser");
        val obj = this.objectService.getObject(objectId);
        val wantedID = obj.realEstate.uploadedFiles.get(0).id;
        this.loginAsUser("file_tenUser2");
        try {
            this.fileService.deleteFile(wantedID ?: -1L);
            fail<String>("Expected file not to be deleted")
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }

    @Test
    @Order(9)
    fun testDeleteFile() {
        this.loginAsUser("file_tenUser");
        val obj = this.objectService.getObject(objectId);
        val wantedID = obj.realEstate.uploadedFiles.get(0).id;
        this.fileService.deleteFile(wantedID ?: -1L);
        assertTrue(true);
    }

    /**
     * Creates a credit for testing
     */
    @Transactional
    fun createObject() {
        val obj = this.objectService.createObject(
            RealEstateInput(
            "1",
            "1",
            "1",
            10000L,
            CreditInput(10000L, 2.0, 2.0, "Bank"),
            Date(),
            2.0,
            24.0,
            "type",
            2000,
            2000,
            "A+",
            2.0,
            true,
            true,
            "Gas",
            "Notes"
        )
        );
        objectId = obj.id ?: -1L;
    }
}