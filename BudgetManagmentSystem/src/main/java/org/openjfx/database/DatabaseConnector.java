package org.openjfx.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static DatabaseConnector instance;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/budget_app";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Berke0204";

    private DatabaseConnector() {}

    public static synchronized DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}