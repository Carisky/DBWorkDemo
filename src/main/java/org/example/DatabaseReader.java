package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseReader {

    private static final String URL = "jdbc:postgresql://localhost:5436/test-db";
    private static final String USER = "sa";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            while (true) {
                readAndDeleteInfoMessages(connection);

                Thread.sleep(1000);
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void readAndDeleteInfoMessages(Connection connection) throws SQLException {
        String sql = "SELECT id, message FROM notice WHERE type='INFO' AND processed=false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String message = resultSet.getString("message");

                System.out.println("Message: " + message);

                deleteMessage(connection, id);
            }
        }
    }

    private static void deleteMessage(Connection connection, int id) throws SQLException {
        String deleteSql = "DELETE FROM notice WHERE id=?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();
        }
    }
}
