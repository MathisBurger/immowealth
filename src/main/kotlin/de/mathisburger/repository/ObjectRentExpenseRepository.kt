package de.mathisburger.repository

import de.mathisburger.entity.ObjectRentExpense
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

/**
 * Repository for object rent expenses
 */
@ApplicationScoped
class ObjectRentExpenseRepository : AbstractRepository<ObjectRentExpense>() {
}