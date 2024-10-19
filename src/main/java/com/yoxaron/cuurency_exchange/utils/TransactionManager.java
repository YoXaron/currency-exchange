package com.yoxaron.cuurency_exchange.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

    public <T> T doInTransaction(TransactionOperation<T> operation) {
        try (Connection connection = ConnectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try {
                T result = operation.execute(connection);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Transaction failed, rolled back.", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error.", e);
        }
    }

    public <T> T doWithoutTransaction(TransactionOperation<T> operation) {
        try (Connection connection = ConnectionManager.getConnection()) {
            return operation.execute(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error.", e);
        }
    }

    @FunctionalInterface
    public interface TransactionOperation<T> {
        T execute(Connection connection) throws SQLException;
    }
}
