package server;

import java.sql.*;

public class UserManager {
    private Connection conn;

    public UserManager() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:chat.db");
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL)";
        conn.createStatement().execute(sql);
    }

    // Passwort ist bereits gehasht!
    public boolean register(String username, String hashedPassword) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);  // Hash kommt direkt vom Client
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Passwort ist bereits gehasht!
    public boolean login(String username, String hashedPassword) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}


