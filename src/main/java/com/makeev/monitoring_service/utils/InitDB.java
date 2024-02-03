package com.makeev.monitoring_service.utils;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.SQLException;

public class InitDB {
    public static void initDB() {
        try (var connection = ConnectionManager.open()) {
            var database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            var liquibase = new Liquibase("db/changelog/changelog.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration is completed successfully");
        } catch (LiquibaseException | SQLException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }

//        String sql5 = """
//                INSERT INTO user_db.users (login, password, admin)
//                    VALUES ('admin', 'admin', true);
//                """;
//        String sql6 = """
//                INSERT INTO user_db.counters (name) VALUES
//                    ('Heating'),
//                    ('Hot Water'),
//                    ('Cold Water');
//                """;
//
    }
}
