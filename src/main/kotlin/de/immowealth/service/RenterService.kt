package de.immowealth.service

import de.immowealth.data.input.CreateRenterInput
import de.immowealth.entity.Renter
import de.immowealth.exception.ParameterException
import de.immowealth.repository.RealEstateRepository
import de.immowealth.repository.RenterRepository
import de.immowealth.voter.RealEstateObjectVoter
import de.immowealth.voter.RenterVoter
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
        val renter = Renter();
        renter.realEstateObject = realEstate;
        renter.firstName = input.firstName;
        renter.lastName = input.lastName;
        renter.birthDay = input.birthDay;
        renter.tenant = realEstate.tenant;
        this.denyUnlessGranted(RenterVoter.CREATE, renter);
        realEstate.renters.add(renter);
        this.entityManager.persist(renter);
        this.entityManager.persist(renter.tenant);
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
            this.entityManager.persist(renter.realEstateObject!!);
        }
        this.delete(renter);
        this.entityManager.flush();

    }
}