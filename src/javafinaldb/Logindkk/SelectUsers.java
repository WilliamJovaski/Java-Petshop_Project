package javafinaldb.Logindkk;

import javafinaldb.DBConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SelectUsers {

    static final String DB_URL = "jdbc:mysql://localhost:3306/finprooop";
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) {
        try {
            // Koneksi ke database
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            // Query SELECT
            String sql = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("=== Data Users ===");

            // Menampilkan hasil
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String role = rs.getString("role");

                System.out.println(
                    "ID: " + id +
                    " | Username: " + username +
                    " | Role: " + role
                );
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
