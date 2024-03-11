package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WarnMessagesUpdater {

    private static final String URL = "jdbc:postgresql://localhost:5436/test-db";
    private static final String USER = "sa";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            while (true) {
                // Выполнение запроса Select
                readAndUpdateWarnMessages(connection);

                // Задержка в 1000 миллисекунд (1 секунда)
                Thread.sleep(1000);
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void readAndUpdateWarnMessages(Connection connection) throws SQLException {
        String selectSql = "SELECT id, message FROM notice WHERE type='WARN' AND processed=false";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectSql);
             ResultSet resultSet = selectStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String message = resultSet.getString("message");

                // Вывод на экран
                System.out.println("Message: " + message);

                // Выполнение запроса Update
                updateMessage(connection, id);
            }
        }
    }

    private static void updateMessage(Connection connection, int id) throws SQLException {
        String updateSql = "UPDATE notice SET processed=false WHERE id=?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            updateStatement.setInt(1, id);
            updateStatement.executeUpdate();
        }
    }
}
