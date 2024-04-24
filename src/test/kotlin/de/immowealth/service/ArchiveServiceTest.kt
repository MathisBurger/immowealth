package de.immowealth.service

import de.immowealth.entity.UserRoles
import de.immowealth.repository.UserRepository
import io.quarkus.security.UnauthorizedException
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.ws.rs.core.SecurityContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.opentest4j.AssertionFailedError

/**
 * Tests the archive service
 */
@QuarkusTest
class ArchiveServiceTest : AbstractServiceTest() {

    @Inject
    override lateinit var securityContext: SecurityContext;

    @Inject
    lateinit var archiveService: ArchiveService;

    @Inject
    lateinit var userService: UserService;

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    @Test
    fun testGetAllArchived() {
        val archived = this.archiveService.getAllArchived();
        assertEquals(archived.size, 0);
    }

    @Test
    fun testRemoveArchivalAsNonAdmin() {
        this.removeAllUsersExceptAdmin(userRepository, entityManager);
        this.loginAsUser("admin")
        val user = this.userService.registerUser(
            "testUser",
            "123",
            "test@test.de",
            mutableListOf()
        );
        this.loginAsUser("testUser");
        try {
            this.archiveService.removeArchival(1L, "test");
            fail("Should not be able to remove archival");
        } catch (e: UnauthorizedException) {
            assertTrue(true)
        } catch (e: AssertionFailedError) {
            throw e;
        }
    }
}