package javafinaldb.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafinaldb.DBConnection;
import javafinaldb.Model.User;

public class UserDao {

    private Connection conn;

    public UserDao() {
        conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("ERROR: Tidak bisa konek database dari UserDao!");
        }
    }

    // =====================================================
    // LOGIN & UPDATE STATUS (Fix image_efb22c)
    // =====================================================
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                
                // Panggil update status saat login berhasil
                updateLoginStatus(u.getId());
                return u;
            }
        } catch (Exception e) {
            System.out.println("ERROR LOGIN:");
            e.printStackTrace();
        }
        return null;
    }

    // Fungsi untuk mengisi kolom status dan last_login
    public void updateLoginStatus(int userId) {
        String sql = "UPDATE users SET status = 'Online', last_login = NOW() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk mengubah status menjadi Offline saat logout
    public void updateLogoutStatus(int userId) {
        String sql = "UPDATE users SET status = 'Offline' WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // CHECK USERNAME EXISTS
    // =====================================================
    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =====================================================
    // INSERT USER
    // =====================================================
    public boolean insertUser(User u) {
        String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =====================================================
    // GET USER BY ID
    // =====================================================
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // =====================================================
    // GET ALL USERS (Bisa untuk tabel Admin)
    // =====================================================
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // =====================================================
    // UPDATE USER
    // =====================================================
    public boolean updateUser(User u) {
        String sql = "UPDATE users SET username=?, password=?, role=? WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.setInt(4, u.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =====================================================
    // DELETE USER
    // =====================================================
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}