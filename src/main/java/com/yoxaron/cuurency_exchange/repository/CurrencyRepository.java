package com.yoxaron.cuurency_exchange.repository;

import com.yoxaron.cuurency_exchange.model.Currency;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyRepository implements Repository<Long, Currency> {

    private static final CurrencyRepository INSTANCE = new CurrencyRepository();

    private static final String FIND_ALL_SQL = """
            SELECT id, code, full_name, sign FROM currencies
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, code, full_name, sign FROM currencies
            WHERE id = ?
            """;

    private static final String FIND_BY_CODE_SQL = """
            SELECT id, code, full_name, sign FROM currencies
            WHERE code = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE currencies SET code = ?, full_name = ?, sign = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM currencies WHERE id = ?
            """;

    @Override
    public List<Currency> findAll(Connection connection) {
        try (var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = statement.executeQuery();
            var currencies = new ArrayList<Currency>();
            while (resultSet.next()) {
                currencies.add(mapRow(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Currency> findById(Connection connection, Long id) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            Currency currency = null;
            if (resultSet.next()) {
                currency = mapRow(resultSet);
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    public Optional<Currency> findByCode(Connection connection, String code) {
        try (var statement = connection.prepareStatement(FIND_BY_CODE_SQL)) {
            statement.setString(1, code);
            var resultSet = statement.executeQuery();
            Currency currency = null;
            if (resultSet.next()) {
                currency = mapRow(resultSet);
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public Currency save(Connection connection, Currency currency) {
        try (var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                currency.setId(keys.getLong("id"));
            }
            return currency;
        } catch (SQLException e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Connection connection, Currency currency) {
        try (var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.setLong(4, currency.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Connection connection, Long id) {
        try (var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    private Currency mapRow(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }

    public static CurrencyRepository getInstance() {
        return INSTANCE;
    }
}
