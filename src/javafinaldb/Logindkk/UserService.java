package javafinaldb.Logindkk;

import java.util.List;
import javafinaldb.Dao.UserDao;
import javafinaldb.Model.User;

public class UserService {

    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }
    public boolean login(String username, String password) {
        User user = userDao.login(username, password);

        if (user != null) {
            Session.id = user.getId();
            Session.username = user.getUsername();
            Session.role = user.getRole();
            return true;
        }

        return false;
    }

    
    public boolean signUp(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username tidak boleh kosong");
            return false;
        }

        if (password == null || password.length() < 6) {
            System.out.println("Password minimal 6 karakter");
            return false;
        }

        if (userDao.usernameExists(username)) {
            System.out.println("Username sudah dipakai");
            return false;
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setRole("customer"); // 🔒 KUNCI ROLE

        return userDao.insertUser(u);
    }

  
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

  
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    
    public boolean updateUser(User u) {

        if (u == null) {
            System.out.println("User tidak boleh null");
            return false;
        }

        if (u.getUsername() == null || u.getUsername().trim().isEmpty()) {
            System.out.println("Username tidak boleh kosong.");
            return false;
        }

        if (u.getPassword() == null || u.getPassword().length() < 6) {
            System.out.println("Password minimal 6 karakter");
            return false;
        }

        if (!u.getRole().equals("admin") && !u.getRole().equals("customer")) {
            System.out.println("Role harus admin atau customer.");
            return false;
        }

        return userDao.updateUser(u);
    }

 
    public boolean deleteUser(int id) {
        if (Session.id == id) {
            System.out.println("Anda tidak bisa menghapus akun yang sedang login.");
            return false;
        }

        return userDao.deleteUser(id);
    }
}