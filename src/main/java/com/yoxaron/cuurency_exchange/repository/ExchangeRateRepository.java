package com.yoxaron.cuurency_exchange.repository;

import com.yoxaron.cuurency_exchange.exception.AlreadyExistsException;
import com.yoxaron.cuurency_exchange.exception.InternalServerError;
import com.yoxaron.cuurency_exchange.model.Currency;
import com.yoxaron.cuurency_exchange.model.ExchangeRate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.postgresql.util.PSQLException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateRepository implements Repository<Long, ExchangeRate> {

    private static final ExchangeRateRepository INSTANCE = new ExchangeRateRepository();

    private static final String FIND_ALL_SQL = """
            SELECT
                er.id,
                c1.id AS b_id,
                c1.full_name AS b_full_name,
                c1.code AS b_code,
                c1.sign AS b_sign,
                c2.id AS t_id,
                c2.full_name AS t_full_name,
                c2.code AS t_code,
                c2.sign AS t_sign,
                er.rate
            FROM exchange_rates er
            JOIN currencies c1 ON er.base_currency_id = c1.id
            JOIN currencies c2 ON er.target_currency_id = c2.id
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE er.id = ?
            """;

    private static final String FIND_BY_PAIR_SQL = FIND_ALL_SQL + """
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
                exchangeRates.add(mapRow(resultSet));
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

    public Optional<ExchangeRate> findByPair(Connection connection, String baseCode, String targetCode) {
        try (var statement = connection.prepareStatement(FIND_BY_PAIR_SQL)) {
            statement.setString(1, baseCode);
            statement.setString(2, targetCode);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRow(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }

    @Override
    public ExchangeRate save(Connection connection, ExchangeRate exchangeRate) {
        try (var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, exchangeRate.getBaseCurrency().getId());
            statement.setLong(2, exchangeRate.getTargetCurrency().getId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                exchangeRate.setId(keys.getLong("id"));
            }
            return exchangeRate;
        } catch (PSQLException e) {
            if (UNIQUE_VIOLATION_CODE.equals(e.getSQLState())) {
                throw new AlreadyExistsException();
            }
            throw new InternalServerError();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }

    @Override
    public boolean update(Connection connection, ExchangeRate exchangeRate) {
        try (var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setBigDecimal(1, exchangeRate.getRate());
            statement.setLong(2, exchangeRate.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }

    @Override
    public boolean delete(Connection connection, Long id) {
        return false;
    }

    private ExchangeRate mapRow(ResultSet resultSet) throws SQLException {
        Currency baseCurrency = new Currency(
                resultSet.getLong("b_id"),
                resultSet.getString("b_code"),
                resultSet.getString("b_full_name"),
                resultSet.getString("b_sign")
        );

        Currency targetCurrency = new Currency(
                resultSet.getLong("t_id"),
                resultSet.getString("t_code"),
                resultSet.getString("t_full_name"),
                resultSet.getString("t_sign")
        );

        long id = resultSet.getLong("id");
        BigDecimal rate = resultSet.getBigDecimal("rate");

        return new ExchangeRate(id, baseCurrency, targetCurrency, rate);
    }

    public static ExchangeRateRepository getInstance() {
        return INSTANCE;
    }
}
