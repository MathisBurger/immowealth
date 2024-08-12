package de.immowealth.voter

import de.immowealth.entity.Archived
import de.immowealth.entity.Renter
import de.immowealth.entity.User
import de.immowealth.service.SecurityService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class RenterVoter : VoterInterface {

    @Inject
    lateinit var securityService: SecurityService;

    companion object {
        val READ = "READ"
        val CREATE = "CREATE"
        val UPDATE = "UPDATE"
        val DELETE = "DELETE"
    }

    override fun <T : Archived> voteOnAttribute(user: User?, attributeName: String, value: T): Boolean {
        if (user == null) {
            return false;
        }
        if (value is Renter) {
            if (value.realEstateObject === null) return true;
            if (attributeName === READ) {
                return this.securityService.isGranted(READ, value.realEstateObject!!);
            }
            return this.securityService.isGranted(UPDATE, value.realEstateObject!!);
        }
        return false;
    }

    override fun votedType(): String {
        return Renter::class.java.toString()
    }
}