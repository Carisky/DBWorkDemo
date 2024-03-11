package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

public class DBWriter {
    private static final String URL = "jdbc:postgresql://localhost:5436/test-db";
    private static final String USER = "sa";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            while (true) {
                // Генерация случайных данных
                String message;
                String type;
                boolean processed = false;

                if (new Random().nextBoolean()) {
                    message = "Новое сообщение от " + LocalDateTime.now();
                    type = "INFO";
                } else {
                    message = "Произошла ошибка в " + LocalDateTime.now();
                    type = "WARN";
                }

                // Выполнение запроса Insert
                insertData(connection, message, type, processed);

                // Задержка в 1000 миллисекунд (1 секунда)
                Thread.sleep(1000);
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void insertData(Connection connection, String message, String type, boolean processed)
            throws SQLException {
        String sql = "INSERT INTO notice (message, type, processed) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, message);
            preparedStatement.setString(2, type);
            preparedStatement.setBoolean(3, processed);
            preparedStatement.executeUpdate();
        }
    }
}
