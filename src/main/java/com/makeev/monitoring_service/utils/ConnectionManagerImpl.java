package com.makeev.monitoring_service.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManagerImpl implements ConnectionManager {

    private String url;
    private String username;
    private String password;

    public ConnectionManagerImpl() {
        Properties properties = new Properties();
        try (var inputStream = ConnectionManagerImpl.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            properties.load(inputStream);
            this.url = properties.getProperty("db.url");
            this.username = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConnectionManagerImpl(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection open() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open connection", e);
        }
    }
}