package com.hanu.util.db;

public class NonQueryResult {
    private final int rowsAffected;

    public NonQueryResult(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }

    @Override
    public String toString() {
        return getClass().getName() + ": " + rowsAffected + " row(s) affected.";
    }
}