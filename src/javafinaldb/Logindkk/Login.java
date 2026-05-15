package javafinaldb.Logindkk;

import javafinaldb.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {

    Connection conn;

    public Login() {
        conn = DBConnection.getConnection();
    }

    public boolean prosesLogin(String username, String password) {
        try {
            String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Session.id = rs.getInt("id"); 
                Session.username = rs.getString("username");
                Session.role = rs.getString("role");
                
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}