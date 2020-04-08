package com.hanu.util.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hanu.exception.InvalidQueryTypeException;

import com.hanu.util.di.Dependency;

/**
 * Handle database connection
 */
@Dependency
public interface DbConnector {
    DbConnector connect() throws SQLException;
    ResultSet executeSelect(String query) throws SQLException, InvalidQueryTypeException;
    <T> T executeScalar(String query) throws SQLException, InvalidQueryTypeException;
    int executeInsert(String query) throws SQLException, InvalidQueryTypeException;
    int executeUpdate(String query) throws SQLException, InvalidQueryTypeException;
    int executeDelete(String query) throws SQLException, InvalidQueryTypeException;
}