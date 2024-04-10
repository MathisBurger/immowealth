package de.immowealth.repository

import de.immowealth.entity.ForeignExchangeRate
import jakarta.enterprise.context.ApplicationScoped
import java.util.Optional

/**
 * Foreign exchange rate repository
 */
@ApplicationScoped
class ForeignExchangeRateRepository : AbstractRepository<ForeignExchangeRate>() {

    /**
     * Finds an exchange rate by symbol.
     *
     * @param The symbol
     */
    fun findBySymbol(symbol: String): Optional<ForeignExchangeRate> {
        return find("symbol", symbol).firstResultOptional()
    }
}