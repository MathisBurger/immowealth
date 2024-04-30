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
import jakarta.persistence.EntityManager
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.jwt.JsonWebToken
import org.wildfly.security.password.Password
import org.wildfly.security.password.PasswordFactory
import org.wildfly.security.password.WildFlyElytronPasswordProvider
import org.wildfly.security.password.interfaces.BCryptPassword
import org.wildfly.security.password.util.ModularCrypt


/**
 * The security service that handles all arround security
 */
@ApplicationScoped
class SecurityService {

    @Inject
    lateinit var userRepository: UserRepository;

    @Inject
    lateinit var voter: Instance<VoterInterface>

    @Inject
    lateinit var ctx: JsonWebToken;

    @Inject
    lateinit var entityManager: EntityManager;

    /**
     * Checks if user is granted for action
     *
     * @param attribute The attribute that should be voted on
     * @param entity The entity that should be voted on
     */
    fun isGranted(attribute: String, entity: Archived? = null): Boolean {
        if (ctx.name === null) {
            return false;
        }
        val rawUser = this.userRepository.findByIdOptional(ctx.name.toLong());
        if (rawUser.isEmpty) {
            return false;
        }
        val user = rawUser.get();
        if (user.roles.contains(attribute)) {
            return true;
        }
        if (entity == null) {
            return false;
        }
        var requiredVoter: VoterInterface? = null;
        for (vt in this.voter) {
            if (vt.votedType() == entity::class.java.toString()) {
                requiredVoter = vt;
                break;
            }
        }
        if (requiredVoter == null) {
            return false;
        }
        return requiredVoter.voteOnAttribute(user, attribute, entity);
    }

    /**
     * Gets the current user that is logged in
     */
    fun getCurrentUser(): User? {
        if (this.ctx.name == null) {
            return null;
        }
        val user =  this.userRepository.findByIdOptional(ctx.name.toLong());
        if (user.isEmpty) {
            return null;
        }
        return user.get();
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