package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Reusable JDBC Connection utility for the Online Quiz System.
 * Connects to MySQL database using standard JDBC driver.
 */
public class DBConnection {

    // Database Connection Credentials
    // Update USER and PASSWORD according to your local MySQL database setup
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";

    static {
        try {
            // Register MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Ensure mysql-connector-j jar is added to your project classpath.");
            e.printStackTrace();
        }
    }

    /**
     * Obtains a new active connection to the quiz_db MySQL database.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
