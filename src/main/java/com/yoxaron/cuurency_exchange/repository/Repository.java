package com.yoxaron.cuurency_exchange.repository;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface Repository<K, E> {

    List<E> findAll(Connection connection);

    Optional<E> findById(Connection connection, K id);

    E save(Connection connection, E entity);

    boolean update(Connection connection, E entity);

    boolean delete(Connection connection, K id);
}
