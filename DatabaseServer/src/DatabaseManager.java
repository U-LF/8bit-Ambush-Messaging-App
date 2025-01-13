// DatabaseManager.java
import java.sql.*;

public class DatabaseManager {
    private static Connection connection;

    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "authentication", new String[] {"TABLE"});

            if (!tables.next()) {
                System.out.println("Authentication table not found. Creating table...");
                try (Statement stmt = conn.createStatement()) {
                    String createTableSQL =
                            "CREATE TABLE authentication (" +
                                    "    username VARCHAR(50) PRIMARY KEY," +
                                    "    display_name VARCHAR(100)," +
                                    "    password VARCHAR(100)," +
                                    "    blocked BOOLEAN DEFAULT FALSE" +
                                    ")";
                    stmt.execute(createTableSQL);
                    System.out.println("Authentication table created successfully.");
                }
            } else {
                System.out.println("Authentication table already exists.");
            }
        }
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/postgres",
                        "postgres",
                        "ahmed"
                );
                System.out.println("Database connection established successfully");
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL JDBC driver not found", e);
            }
        }
        return connection;
    }

    public String handleSignup(String username, String displayName, String password) {
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement("SELECT username FROM authentication WHERE username = ?")) {

            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return "error:Username already exists";
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO authentication (username, display_name, password) VALUES (?, ?, ?)")) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, displayName);
                insertStmt.setString(3, password); // In production, use password hashing
                insertStmt.executeUpdate();
                return "success:Account created successfully";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:Signup failed: " + e.getMessage();
        }
    }

    public String handleLogin(String username, String password) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM authentication WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password); // In production, use password verification

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "success:Login successful";
            } else {
                return "error:Invalid username or password";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:Login failed: " + e.getMessage();
        }
    }
}

