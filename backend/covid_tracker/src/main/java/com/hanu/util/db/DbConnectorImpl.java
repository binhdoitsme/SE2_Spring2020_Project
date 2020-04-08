package com.hanu.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hanu.exception.InvalidQueryTypeException;
import com.hanu.util.configuration.Configuration;

public class DbConnectorImpl implements DbConnector {
    // constants
    private static final String DRIVER = Configuration.get("db.driver");
    private static final String CONNECTION_STRING = Configuration.get("db.connectionstring");
    private static final String DB_USER = Configuration.get("db.user");
    private static final String DB_PASSWORD = Configuration.get("db.password");

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
        return connection.createStatement().executeQuery(query);
    }

    @Override @SuppressWarnings("unchecked")
    public <T> T executeScalar(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.SELECT);
        ResultSet rs = connection.createStatement().executeQuery(query);
        // scalar query returns one value only
        rs.next();
        return (T) rs.getObject(1);
    }

    @Override
    public int executeInsert(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.INSERT);
        return connection.createStatement().executeUpdate(query);
    }

    @Override
    public int executeUpdate(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.UPDATE);
        return connection.createStatement().executeUpdate(query);
    }

    @Override
    public int executeDelete(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.DELETE);
        return connection.createStatement().executeUpdate(query);
    }

    private void throwExceptionIfInvalidQueryType(String query, DbQueryType type)
        throws InvalidQueryTypeException {
        if (!validateQueryType(query, type)) {
            throw new InvalidQueryTypeException();
        }
    }

    private boolean validateQueryType(String query, DbQueryType type) {
        return query.toLowerCase().startsWith(type.name().toLowerCase());
    }
}

enum DbQueryType {
    SELECT, INSERT, UPDATE, DELETE
}