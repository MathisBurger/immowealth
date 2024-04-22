package de.immowealth.service

import de.immowealth.entity.Archived
import de.immowealth.entity.User
import de.immowealth.repository.UserRepository
import de.immowealth.voter.VoterInterface
import io.quarkus.elytron.security.common.BcryptUtil
import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.inject.Inject
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.core.SecurityContext
import org.wildfly.security.password.Password
import org.wildfly.security.password.PasswordFactory
import org.wildfly.security.password.WildFlyElytronPasswordProvider
import org.wildfly.security.password.interfaces.BCryptPassword
import org.wildfly.security.password.util.ModularCrypt


/**
 * The security service that handles all arround security
 */
@ApplicationScoped
class SecurityService  : AbstractService() {

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    lateinit var voter: Instance<VoterInterface>

    @Inject
    lateinit var ctx: SecurityContext;

    /**
     * Checks if user is granted for action
     *
     * @param attribute The attribute that should be voted on
     * @param entity The entity that should be voted on
     */
    fun isGranted(attribute: String, entity: Archived): Boolean {
        var requiredVoter: VoterInterface? = null;
        for (vt in this.voter) {
            if (vt.votedType() === entity::class.java.toString()) {
                requiredVoter = vt;
                break;
            }
        }
        if (requiredVoter == null) {
            return false;
        }
        if (ctx.userPrincipal === null) {
            return false;
        }
        val user = this.userRepository.findByUserName(ctx.userPrincipal.name).get();
        return requiredVoter.voteOnAttribute(user, attribute, entity);
    }


    /**
     * Logs in a user
     *
     * @param username The username of the user
     * @param password The password of the user
     */
    fun login(username: String, password: String): String {
        val user = this.userRepository.findByUserName(username);
        if (user.isEmpty) {
            throw NotAuthorizedException("Wrong credentials");
        }
        if (BcryptUtil.matches(password, user.get().password)) {
            return this.generateJWT(user.get());
        }
        throw NotAuthorizedException("Wrong credentials");
    }

    /**
     * Generates the JWT for a user
     *
     * @param user The user that the JWT is generated for
     */
    fun generateJWT(user: User): String {
        return Jwt
            .upn(user.id!!.toString())
            .sign();
    }
}