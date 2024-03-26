package de.mathisburger.repository

import de.mathisburger.entity.ForeignExchangeRate
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.Optional

/**
 * Foreign exchange rate repository
 */
@ApplicationScoped
class ForeignExchangeRateRepository : PanacheRepository<ForeignExchangeRate> {

    /**
     * Finds an exchange rate by symbol.
     *
     * @param The symbol
     */
    fun findBySymbol(symbol: String): Optional<ForeignExchangeRate> {
        return find("symbol", symbol).firstResultOptional()
    }
}