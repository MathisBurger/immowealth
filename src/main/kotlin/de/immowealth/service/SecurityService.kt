package de.immowealth.service

import de.immowealth.entity.Archived
import de.immowealth.entity.User
import de.immowealth.repository.UserRepository
import de.immowealth.util.RandomUtils
import de.immowealth.voter.VoterInterface
import io.quarkus.elytron.security.common.BcryptUtil
import io.smallrye.common.annotation.Blocking
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipalFactory
import io.smallrye.jwt.auth.principal.JWTAuthContextInfo
import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.JsonWebToken
import org.wildfly.security.password.Password
import org.wildfly.security.password.PasswordFactory
import org.wildfly.security.password.WildFlyElytronPasswordProvider
import org.wildfly.security.password.interfaces.BCryptPassword
import org.wildfly.security.password.util.ModularCrypt
import java.util.*
import kotlin.reflect.typeOf


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

    @Inject
    lateinit var mailService: MailService;

    @Inject
    lateinit var jwtParser: JWTParser;

    @ConfigProperty(name = "immowealth.applicationHost")
    lateinit var applicationHost: String;

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
        var rawUser: Optional<User> = Optional.empty();
        try {
            rawUser = this.userRepository.findByIdOptional(ctx.name.toLong())
        } catch (e: NumberFormatException) {
            rawUser = this.userRepository.findByUserName(ctx.name);
        }
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
     * Gets the current user ID by token
     *
     * @param token The JWT token
     */
    fun getCurrentUserIdByToken(token: String): Long? {
        val parsedToken = this.jwtParser.parse(token);
        try {
            return parsedToken.name.toLong()
        } catch (e: NumberFormatException) {
            return null;
        }
    }

    /**
     * Gets the current user that is logged in
     */
    fun getCurrentUser(): User? {
        if (this.ctx.name == null) {
            return null;
        }
        return this.getUser(ctx.name);
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

    /**
     * Resets the password of the user and sends an email
     *
     * @param username The username of the user that should be resetted
     */
    @Transactional
    fun resetPassword(username: String) {
        val user = this.userRepository.findByUserName(username);
        if (user.isEmpty) {
            return;
        }
        val fetchedUser = user.get();
        fetchedUser.password = RandomUtils.getRandomString(254);
        this.entityManager.persist(fetchedUser);
        this.entityManager.flush();
        val jwt = this.generateJWT(fetchedUser);
        val url = this.applicationHost + "/resetPassword?session=" + jwt;
        val mailTemplate = "<a href=\"$url\">Reset password</a>";
        this.mailService.sendMailHTML("Password reset", mailTemplate, fetchedUser.email);
    }

    /**
     * Sets the new password of a user
     *
     * @param username The username of the user
     * @param password The new password of the user
     */
    @Transactional
    fun setNewPassword(id: Long, password: String) {
        val user = this.userRepository.findByIdOptional(id);
        if (user.isEmpty) {
            return;
        }
        val fetchedUser = user.get();
        fetchedUser.password = BcryptUtil.bcryptHash(password);
        this.entityManager.persist(fetchedUser);
        this.entityManager.flush();
    }

    /**
     * Gets the user by name (ID)
     *
     * @param name The ID of the user
     */
    private fun getUser(name: String): User? {
        var rawUser: Optional<User> = Optional.empty();
        try {
            rawUser = this.userRepository.findByIdOptional(name.toLong())
        } catch (e: NumberFormatException) {
            rawUser = this.userRepository.findByUserName(name);
        }
        if (rawUser.isEmpty) {
            return null;
        }
        return rawUser.get();
    }
}