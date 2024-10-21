package com.yoxaron.cuurency_exchange.repository;

import com.yoxaron.cuurency_exchange.exception.InternalServerError;
import com.yoxaron.cuurency_exchange.model.Currency;
import com.yoxaron.cuurency_exchange.model.ExchangeRate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateRepository implements Repository<Long, ExchangeRate> {

    private static final ExchangeRateRepository INSTANCE = new ExchangeRateRepository();
    private final CurrencyRepository currencyRepository = CurrencyRepository.getInstance();

    private static final String FIND_ALL_SQL = """
            SELECT id, base_currency_id, target_currency_id, rate FROM exchange_rates
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, base_currency_id, target_currency_id, rate FROM exchange_rates
            WHERE id = ?
            """;

    private static final String FIND_BY_PAIR_SQL = """
            SELECT er.id, c1.id, c1.full_name, c1.code, c1.sign, c2.id, c2.full_name, c2.code, c2.sign, er.rate
            FROM exchange_rates er
            JOIN currencies c1 ON er.base_currency_id = c1.id
            JOIN currencies c2 ON er.target_currency_id = c2.id
            WHERE c1.code = ? AND c2.code = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE exchange_rates SET rate = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM exchange_rates WHERE id = ?
            """;

    @Override
    public List<ExchangeRate> findAll(Connection connection) {
        try (var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = statement.executeQuery();
            var exchangeRates = new ArrayList<ExchangeRate>();
            while (resultSet.next()) {
                exchangeRates.add(mapRow(connection, resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }

    @Override
    public Optional<ExchangeRate> findById(Connection connection, Long id) {
        return Optional.empty();
    }

    @Override
    public ExchangeRate save(Connection connection, ExchangeRate entity) {
        return null;
    }

    @Override
    public boolean update(Connection connection, ExchangeRate entity) {
        return false;
    }

    @Override
    public boolean delete(Connection connection, Long id) {
        return false;
    }

    private ExchangeRate mapRow(Connection connection, ResultSet resultSet) throws SQLException {
        var baseCurrencyId = resultSet.getLong("base_currency_id");
        Currency baseCurrency = currencyRepository.findById(connection, baseCurrencyId).orElseThrow();

        var targetCurrencyId = resultSet.getLong("target_currency_id");
        Currency targetCurrency = currencyRepository.findById(connection, targetCurrencyId).orElseThrow();

        var id = resultSet.getLong("id");
        var rate = resultSet.getBigDecimal("rate");
        return new ExchangeRate(id, baseCurrency, targetCurrency, rate);
    }

    public static ExchangeRateRepository getInstance() {
        return INSTANCE;
    }
}
