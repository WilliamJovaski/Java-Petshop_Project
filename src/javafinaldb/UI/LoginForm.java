package javafinaldb.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import javafinaldb.Dao.UserDao;
import javafinaldb.Logindkk.Session;
import javafinaldb.Model.User;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnSignup;
    private UserDao userDao = new UserDao();

    public LoginForm() {
        setTitle("Pet Shop - Login");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 45, 30, 45));
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // --- ICON ---
        JLabel lblIcon = new JLabel("🐾", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(0, 0, 10, 0);
        mainPanel.add(lblIcon, gbc);

        // --- TITLE ---
        JLabel lblTitle = new JLabel("Welcome Back", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(new Color(44, 62, 80));
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(0, 0, 40, 0);
        mainPanel.add(lblTitle, gbc);

        // --- USERNAME ---
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUser.setForeground(Color.GRAY);
        gbc.gridy = 2;
        gbc.insets = new java.awt.Insets(10, 0, 0, 0);
        mainPanel.add(lblUser, gbc);

        txtUsername = new JTextField();
        styleUnderlineField(txtUsername);
        gbc.gridy = 3;
        gbc.insets = new java.awt.Insets(0, 0, 25, 0);
        mainPanel.add(txtUsername, gbc);

        // --- PASSWORD ---
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPass.setForeground(Color.GRAY);
        gbc.gridy = 4;
        gbc.insets = new java.awt.Insets(0, 0, 0, 0);
        mainPanel.add(lblPass, gbc);

        txtPassword = new JPasswordField();
        styleUnderlineField(txtPassword);
        gbc.gridy = 5;
        gbc.insets = new java.awt.Insets(0, 0, 35, 0);
        mainPanel.add(txtPassword, gbc);

        // --- LOGIN BUTTON ---
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(0, 45));
        gbc.gridy = 6;
        gbc.insets = new java.awt.Insets(10, 0, 15, 0);
        mainPanel.add(btnLogin, gbc);

        // --- SIGNUP LINK ---
        btnSignup = new JButton("New here? Create an Account");
        btnSignup.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnSignup.setForeground(Color.GRAY);
        btnSignup.setContentAreaFilled(false);
        btnSignup.setBorderPainted(false);
        btnSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 7;
        gbc.insets = new java.awt.Insets(0, 0, 0, 0);
        mainPanel.add(btnSignup, gbc);

        // --- LOGIKA LOGIN (SINKRON DENGAN STATUS DB) ---
        btnLogin.addActionListener(e -> {
            String userIn = txtUsername.getText();
            String passIn = new String(txtPassword.getPassword());

            if (userIn.isEmpty() || passIn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!");
                return;
            }

            User user = userDao.login(userIn, passIn);
            
            if (user != null) {
                // Simpan ke Session
                Session.id = user.getId(); 
                Session.username = user.getUsername();
                Session.role = user.getRole();
                
                // --- UPDATE STATUS DI DATABASE ---
                // Mengupdate status menjadi 'Online' dan mengisi 'last_login'
                userDao.updateLoginStatus(Session.id); 
                
                // Cek Role
                if (user.getRole().equalsIgnoreCase("admin")) {
                    JOptionPane.showMessageDialog(this, "Selamat Datang Admin!");
                    new AdminForm().setVisible(true); 
                } else {
                    JOptionPane.showMessageDialog(this, "Login Berhasil!");
                    new CustomerForm().setVisible(true); 
                }
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Login Gagal! Akun salah.");
            }
        });

        btnSignup.addActionListener(e -> {
            new SignupForm().setVisible(true);
            dispose();
        });
    }

    private void styleUnderlineField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBackground(Color.WHITE);
        field.setOpaque(true);
        field.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
        field.setPreferredSize(new Dimension(0, 35));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}