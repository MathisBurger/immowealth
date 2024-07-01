package de.immowealth.voter

import de.immowealth.entity.*
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
        val READ = "READ"
        val SWITCH_USERS = "SWITCH_USERS"
    }

    @Inject
    lateinit var entityManager: EntityManager;

    override fun <T : Archived> voteOnAttribute(user: User?, attributeName: String, value: T): Boolean {
        if (user == null) {
            return false;
        }
        if (user.roles.contains(UserRoles.ADMIN)) {
            return true;
        }
        if (attributeName === TenantVoter.CREATE || attributeName === TenantVoter.SWITCH_USERS) {
            return user.roles.contains(UserRoles.ADMIN);
        }
        if (attributeName === READ && value is Tenant) {
            return user.tenant?.id === value.id;
        }
        return false;
    }

    override fun votedType(): String {
        return Tenant::class.java.toString()
    }
}