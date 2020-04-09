package com.hanu.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.configuration.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbConnectorImpl implements DbConnector {
    // constants
    private static final String DRIVER = Configuration.get("db.driver");
    private static final String CONNECTION_STRING = Configuration.get("db.connectionstring");
    private static final String DB_USER = Configuration.get("db.user");
    private static final String DB_PASSWORD = Configuration.get("db.password");

    private static final Logger logger = LoggerFactory.getLogger(DbConnectorImpl.class);

    private Connection connection;

    public DbConnectorImpl() throws ClassNotFoundException {
        loadDriver();
    }

    private void loadDriver() throws ClassNotFoundException {
        Class.forName(DRIVER);
    }

    @Override
    public DbConnector connect() throws SQLException {
        connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        return this;
    }

    @Override
    public ResultSet executeSelect(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.SELECT);
        logQuery(query);
        return connection.createStatement().executeQuery(query);
    }

    @Override @SuppressWarnings("unchecked")
    public <T> T executeScalar(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.SELECT);
        logQuery(query);
        ResultSet rs = connection.createStatement().executeQuery(query);
        // scalar query returns one value only
        rs.next();
        return (T) rs.getObject(1);
    }

    @Override
    public int executeInsert(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.INSERT);
        logQuery(query);
        return connection.createStatement().executeUpdate(query);
    }

    @Override
    public int executeUpdate(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.UPDATE);
        logQuery(query);
        return connection.createStatement().executeUpdate(query);
    }

    @Override
    public int executeDelete(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.DELETE);
        logQuery(query);
        return connection.createStatement().executeUpdate(query);
    }

    private void throwExceptionIfInvalidQueryType(String query, DbQueryType type)
        throws InvalidQueryTypeException {
        if (!validateQueryType(query, type)) {
            throw new InvalidQueryTypeException();
        }
    }

    private boolean validateQueryType(String query, DbQueryType type) {
        return query.toLowerCase().trim().startsWith(type.name().toLowerCase());
    }

    public static void logQuery(String query) {
        logger.info(query);
    }
}

enum DbQueryType {
    SELECT, INSERT, UPDATE, DELETE
}