package de.immowealth.service

import de.immowealth.data.input.CreateRenterInput
import de.immowealth.entity.Chat
import de.immowealth.entity.Renter
import de.immowealth.entity.User
import de.immowealth.entity.UserRoles
import de.immowealth.exception.ParameterException
import de.immowealth.repository.RealEstateRepository
import de.immowealth.repository.RenterRepository
import de.immowealth.repository.UserRepository
import de.immowealth.voter.RealEstateObjectVoter
import de.immowealth.voter.RenterVoter
import io.quarkus.elytron.security.common.BcryptUtil
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional

/**
 * Service handling renter actions
 */
@ApplicationScoped
class RenterService : AbstractService() {

    @Inject
    lateinit var realEstateRepository: RealEstateRepository;

    @Inject
    lateinit var renterRepository: RenterRepository;

    @Inject
    lateinit var userRepository: UserRepository;


    /**
     * Creates a renter on real estate object
     *
     * @param id The ID of the real estate object
     * @param input The creation input
     */
    @Transactional
    fun createRenterOnObject(input: CreateRenterInput): Renter {
        val obj = this.realEstateRepository.findByIdOptional(input.objectID);
        if (obj.isEmpty) {
            throw ParameterException("The given real estate object does not exist");
        }
        val realEstate = obj.get();
        this.denyUnlessGranted(RealEstateObjectVoter.READ, realEstate);
        val userExists = this.userRepository.findByUserName(input.username).isPresent;
        if (userExists) {
            throw ParameterException("Username already in use");
        }
        val renter = Renter();
        renter.realEstateObject = realEstate;
        if (realEstate.chat === null) {
            val chat = Chat()
            chat.realEstateObject = realEstate;
            chat.isRenterChat = true;
            this.entityManager.persist(chat);
            realEstate.chat = chat;
        }
        renter.firstName = input.firstName;
        renter.lastName = input.lastName;
        renter.birthDay = input.birthDay;
        renter.tenant = realEstate.tenant;
        val user = User()
        user.username = input.username;
        user.password = BcryptUtil.bcryptHash(input.password)
        user.email = input.email;
        user.renter = renter;
        user.roles = mutableListOf(UserRoles.RENTER)
        renter.user = user;
        this.denyUnlessGranted(RenterVoter.CREATE, renter);
        realEstate.renters.add(renter);
        this.entityManager.persist(user);
        this.entityManager.persist(renter);
        this.entityManager.persist(realEstate);
        this.entityManager.flush();
        return renter;
    }

    /**
     * Deletes a renter from the object and from the system itself
     */
    @Transactional
    fun deleteRenterFromObject(renterId: Long) {
        val renterOption = this.renterRepository.findByIdOptional(renterId);
        if (renterOption.isEmpty) {
            throw ParameterException("The given renter does not exist");
        }
        val renter = renterOption.get();
        this.denyUnlessGranted(RenterVoter.DELETE, renter);
        if (renter.realEstateObject !== null) {
            renter.realEstateObject!!.renters.remove(renter);
            if (renter.realEstateObject!!.renters.size == 0 && renter.realEstateObject!!.chat !== null) {
                val chat = renter.realEstateObject!!.chat;
                this.entityManager.remove(chat);
                renter.realEstateObject!!.chat = null;
                this.entityManager.persist(renter.realEstateObject!!);

            } else {
                this.entityManager.persist(renter.realEstateObject!!);
            }

        }
        this.entityManager.remove(renter);
        this.entityManager.flush();

    }
}