package de.immowealth.service

import de.immowealth.entity.ObjectRentExpense
import de.immowealth.entity.RealEstateObject
import de.immowealth.entity.enum.AutoPayInterval
import de.immowealth.entity.enum.MailEntityContext
import de.immowealth.entity.enum.MailerSettingAction
import de.immowealth.entity.enum.ObjectRentType
import de.immowealth.exception.ParameterException
import de.immowealth.repository.ObjectRentExpenseRepository
import de.immowealth.repository.RealEstateRepository
import de.immowealth.util.AutoBookingUtils
import de.immowealth.voter.ObjectRentExpenseVoter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
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
        exp.tenant = this.securityService.getCurrentUser()?.tenant;
        this.denyUnlessGranted(ObjectRentExpenseVoter.CREATE, exp)
        this.entityManager.persist(exp);
        obj.expenses.add(exp);
        this.entityManager.persist(obj);
        this.entityManager.flush();
        this.log.writeLog("Added rent expense ($expense€, $type, $name) to object with ID ${obj.id}");
        this.mail.sendEntityActionMail(
            "Added rent expense",
            "Added rent expense ($expense€, $type, $name) to object with ID ${obj.id}",
            "kontakt@mathis-burger.de",
            MailEntityContext.realEstateObject,
            MailerSettingAction.UPDATE_ONLY
        );
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
        this.denyUnlessGranted(ObjectRentExpenseVoter.UPDATE, exp);
        this.entityManager.persist(exp);
        this.entityManager.flush();
        this.log.writeLog("Updated rent expense with ID ${exp.id}");
        this.mail.sendEntityActionMail(
            "Updated rent expense",
            "Updated rent expense with ID ${exp.id}",
            "kontakt@mathis-burger.de",
            MailEntityContext.realEstateObject,
            MailerSettingAction.UPDATE_ONLY
        );
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
            this.denyUnlessGranted(ObjectRentExpenseVoter.DELETE, obj)
            obj.realEstateObject.expenses.remove(obj);
            this.entityManager.persist(obj.realEstateObject);
            this.entityManager.remove(obj);
            this.entityManager.flush();
            this.log.writeLog("Deleted rent expense with ID ${obj.id}");
            this.mail.sendEntityActionMail(
                "Deleted rent expense",
                "Deleted rent expense with ID ${obj.id}",
                "kontakt@mathis-burger.de",
                MailEntityContext.realEstateObject,
                MailerSettingAction.UPDATE_ONLY
            );
        }
    }

    /**
     * Sets auto booking by expenses.
     *
     * @param objectId The object ID
     */
    @Transactional
    fun setAutoBookingByExpenses(objectId: Long): RealEstateObject {
        val obj = this.objectRepository.findById(objectId);
        this.denyUnlessGranted(ObjectRentExpenseVoter.UPDATE, obj)
        var expenseCount: Double = 0.0;
        for (exp in obj.expenses) {
            if (listOf(ObjectRentType.INTEREST_RATE, ObjectRentType.REDEMPTION_RATE).contains(exp.type!!)) {
                expenseCount += exp.value!!;
            }
        }
        obj.credit?.autoPayAmount = expenseCount;
        obj.credit?.autoPayInterval = AutoPayInterval.MONTHLY;
        obj.credit?.nextCreditRate = AutoBookingUtils.getNextAutoPayIntervalDate(AutoPayInterval.MONTHLY);
        this.entityManager.persist(obj.credit!!);
        this.entityManager.flush();
        this.log.writeLog("Updated auto booking by rent expenses on object with ID ${obj.id}");
        return obj;
    }
}