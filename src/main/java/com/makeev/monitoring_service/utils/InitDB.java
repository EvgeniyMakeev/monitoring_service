package com.makeev.monitoring_service.utils;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.SQLException;

public class InitDB {

    private final ConnectionManager connectionManager = new ConnectionManagerImpl();

    public void initDB() {
        try (var connection = connectionManager.open()) {
            var database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            var liquibase = new Liquibase("db/changelog/changelog.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration is completed successfully");
        } catch (LiquibaseException | SQLException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
    }
}
