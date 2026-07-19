package com.example.fitness.common.database;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseMigrationTest {

    private static final List<String> CORE_TABLES = List.of(
            "user_account",
            "user_profile",
            "body_metric_record",
            "workout_record",
            "workout_exercise_entry",
            "workout_set_entry",
            "workout_template",
            "workout_template_item",
            "diet_record",
            "diet_food_entry",
            "food_template",
            "training_plan",
            "knowledge_item"
    );

    @Test
    void createsCoreTables() throws SQLException {
        String jdbcUrl = "jdbc:h2:mem:fitness_migration;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1";

        Flyway.configure()
                .dataSource(jdbcUrl, "sa", "")
                .locations("classpath:db/migration")
                .load()
                .migrate();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, "sa", "")) {
            for (String tableName : CORE_TABLES) {
                assertThat(tableExists(connection, tableName))
                        .as("表 %s 应该由 Flyway 迁移创建", tableName)
                        .isTrue();
            }
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        try (var resultSet = connection.getMetaData().getTables(null, null, tableName, new String[]{"TABLE"})) {
            return resultSet.next();
        }
    }
}
