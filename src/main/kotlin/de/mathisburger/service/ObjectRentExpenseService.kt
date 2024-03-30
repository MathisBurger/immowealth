package de.mathisburger.service

import de.mathisburger.entity.ObjectRentExpense
import de.mathisburger.entity.enum.ObjectRentType
import de.mathisburger.exception.ParameterException
import de.mathisburger.repository.ObjectRentExpenseRepository
import de.mathisburger.repository.RealEstateRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

/**
 * Service that handles all actions on object rent expenses
 */
@ApplicationScoped
class ObjectRentExpenseService : AbstractService() {

    @Inject
    lateinit var objectRepository: RealEstateRepository;

    @Inject
    lateinit var objectRentExpenseRepository: ObjectRentExpenseRepository;

    @Inject
    lateinit var entityManager: EntityManager;

    /**
     * Adds a new object rent expense to rental object
     *
     * @param objectId The ID of the object
     * @param expense The monetary value of the expense
     * @param type The rent type
     * @param name The name of the expense
     */
    @Transactional
    fun addObjectRentExpenseToObject(objectId: Long, expense: Double, type: ObjectRentType, name: String): ObjectRentExpense {
        val obj = this.objectRepository.findById(objectId) ?: throw ParameterException("Object does not exist");
        val exp = ObjectRentExpense();
        exp.value = expense;
        exp.type = type;
        exp.name = name;
        exp.realEstateObject = obj;
        this.entityManager.persist(exp);
        obj.expenses.add(exp);
        this.entityManager.persist(obj);
        this.entityManager.flush();
        return exp;
    }

    /**
     * Updates an rent expense object
     *
     * @param id The ID of the expense
     * @param expense The monetary value
     * @param type The expense type
     * @param name The name of the expense
     */
    @Transactional
    fun updateRentExpense(id: Long, expense: Double?, type: ObjectRentType?, name: String?): ObjectRentExpense {
        val exp = this.objectRentExpenseRepository.findById(id) ?: throw ParameterException("Invalid ID");
        exp.value = expense ?: exp.value;
        exp.name = name ?: exp.name;
        exp.type = type ?: exp.type;
        this.entityManager.persist(exp);
        this.entityManager.flush();
        return exp;
    }

    /**
     * Deletes an rent expense
     *
     * @param id The ID of the expense that should be deleted
     */
    @Transactional
    fun deleteExpense(id: Long) {
        val obj = this.objectRentExpenseRepository.findById(id);
        if (obj != null) {
            obj.realEstateObject.expenses.remove(obj);
            this.entityManager.persist(obj.realEstateObject);
            this.entityManager.remove(obj);
            this.entityManager.flush();
        }
    }
}