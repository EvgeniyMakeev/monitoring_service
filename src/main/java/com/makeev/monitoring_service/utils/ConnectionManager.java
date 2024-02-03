package com.makeev.monitoring_service.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String URL_KEY = "db.url";
    private static final String USER_NAME = "db.username";
    private static final String PASSWORD = "db.password";

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USER_NAME),
                    PropertiesUtil.get(PASSWORD));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionManager(){}
}
