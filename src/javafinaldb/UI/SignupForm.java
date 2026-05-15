package javafinaldb.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import javafinaldb.Dao.UserDao;
import javafinaldb.Model.User;

public class SignupForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSignup, btnBack;
    private UserDao userDao = new UserDao();

    public SignupForm() {
        setTitle("Create Account");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        // Panel Utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout()); // Menggunakan GridBag agar presisi
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(5, 0, 5, 0);
        gbc.gridx = 0;

        // --- ICON ---
        JLabel lblIcon = new JLabel("✨", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        gbc.gridy = 0;
        mainPanel.add(lblIcon, gbc);

        // --- TITLE ---
        JLabel lblTitle = new JLabel("Join Us", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(0, 0, 30, 0);
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
        gbc.insets = new java.awt.Insets(0, 0, 20, 0);
        mainPanel.add(txtUsername, gbc);

        // --- PASSWORD ---
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPass.setForeground(Color.GRAY);
        gbc.gridy = 4;
        mainPanel.add(lblPass, gbc);

        txtPassword = new JPasswordField();
        styleUnderlineField(txtPassword);
        gbc.gridy = 5;
        mainPanel.add(txtPassword, gbc);

        // --- SIGNUP BUTTON ---
        btnSignup = new JButton("CREATE ACCOUNT");
        btnSignup.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSignup.setBackground(new Color(41, 128, 185)); // Biru
        btnSignup.setForeground(Color.WHITE);
        btnSignup.setFocusPainted(false);
        btnSignup.setBorderPainted(false);
        btnSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSignup.setPreferredSize(new Dimension(0, 45));
        
        gbc.gridy = 6;
        gbc.insets = new java.awt.Insets(40, 0, 10, 0);
        mainPanel.add(btnSignup, gbc);

        // --- BACK BUTTON ---
        btnBack = new JButton("Already have an account? Login");
        btnBack.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnBack.setForeground(Color.GRAY);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridy = 7;
        gbc.insets = new java.awt.Insets(0, 0, 0, 0);
        mainPanel.add(btnBack, gbc);

        // --- LOGIC ---
        btnSignup.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi kolom dulu ya!");
                return;
            }

            if (userDao.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username sudah ada!");
            } else {
                User u = new User(0, username, password, "customer");
                if (userDao.insertUser(u)) {
                    JOptionPane.showMessageDialog(this, "Berhasil Daftar!");
                    new LoginForm().setVisible(true);
                    dispose();
                }
            }
        });

        btnBack.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }

    // Method Kunci untuk Gaya Underline (Hanya Garis Bawah)
    private void styleUnderlineField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setBackground(Color.WHITE);
        field.setOpaque(true);
        // MatteBorder(atas, kiri, bawah, kanan, warna)
        // Kita hanya beri nilai di 'bawah' saja (2 pixel hitam)
        field.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
    }
}