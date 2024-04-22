package de.immowealth.voter

import de.immowealth.entity.Archived
import de.immowealth.entity.Tenant
import de.immowealth.entity.User
import de.immowealth.entity.UserRoles
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager

/**
 * Votes on tenants
 */
@ApplicationScoped
class TenantVoter : VoterInterface {

    companion object {
        final val CREATE = "CREATE";
    }

    @Inject
    lateinit var entityManager: EntityManager;

    override fun <T : Archived> voteOnAttribute(user: User, attributeName: String, value: T): Boolean {
        if (attributeName === TenantVoter.CREATE) {
            return user.roles.contains(UserRoles.ADMIN);
        }
        return false;
    }

    override fun votedType(): String {
        return Tenant::class.java.toString()
    }
}