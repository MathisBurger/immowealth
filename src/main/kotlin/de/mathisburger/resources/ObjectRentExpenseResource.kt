package de.mathisburger.resources

import de.mathisburger.entity.ObjectRentExpense
import de.mathisburger.entity.RealEstateObject
import de.mathisburger.entity.enum.ObjectRentType
import de.mathisburger.service.ObjectRentExpenseService
import jakarta.inject.Inject
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation

/**
 * GraphQL resource for rent expenses
 */
@GraphQLApi
class ObjectRentExpenseResource {

    @Inject
    lateinit var objectRentExpenseService: ObjectRentExpenseService;

    /**
     * Adds new rent expenses to real estate object
     */
    @Mutation
    fun addObjectRentExpenseToObject(objectId: Long, expense: Double, type: ObjectRentType, name: String): ObjectRentExpense {
        return this.objectRentExpenseService.addObjectRentExpenseToObject(objectId, expense, type, name);
    }

    /**
     * Updates rent expense object
     */
    @Mutation
    fun updateRentExpense(id: Long, expense: Double?, type: ObjectRentType?, name: String?): ObjectRentExpense {
        return this.objectRentExpenseService.updateRentExpense(id, expense, type, name);
    }

    /**
     * Deletes rent expense with ID.
     */
    @Mutation
    fun deleteRentExpense(id: Long): Boolean {
        this.objectRentExpenseService.deleteExpense(id);
        return true;
    }

    /**
     * Sets auto booking by expenses.
     */
    @Mutation
    fun setAutoBookingByExpenses(id: Long): RealEstateObject {
        return this.objectRentExpenseService.setAutoBookingByExpenses(id);
    }
}