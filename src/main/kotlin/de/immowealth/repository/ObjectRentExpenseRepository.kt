package de.immowealth.repository

import de.immowealth.entity.ObjectRentExpense
import jakarta.enterprise.context.ApplicationScoped

/**
 * Repository for object rent expenses
 */
@ApplicationScoped
class ObjectRentExpenseRepository : AbstractRepository<ObjectRentExpense>() {
}