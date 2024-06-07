package de.immowealth.service

import de.immowealth.data.input.CreateRenterInput
import de.immowealth.entity.Renter
import de.immowealth.exception.ParameterException
import de.immowealth.repository.RealEstateRepository
import de.immowealth.voter.RealEstateObjectVoter
import de.immowealth.voter.RenterVoter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
 * Service handling renter actions
 */
@ApplicationScoped
class RenterService : AbstractService() {

    @Inject
    lateinit var realEstateRepository: RealEstateRepository;

    /**
     * Creates a renter on real estate object
     *
     * @param id The ID of the real estate object
     * @param input The creation input
     */
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
        this.denyUnlessGranted(RenterVoter.CREATE, renter);
        realEstate.renters.add(renter);
        this.entityManager.persist(renter);
        this.entityManager.persist(realEstate);
        this.entityManager.flush();
        return renter;
    }
}