package javafinaldb.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javafinaldb.Dao.TransaksiDao;
import javafinaldb.Dao.UserDao;
import javafinaldb.Logindkk.Session;

public class CustomerForm extends JFrame {
    private TransaksiDao trxDao = new TransaksiDao();
    private UserDao userDao = new UserDao(); 
    private JPanel mainContent;
    private JLabel lblSpend; // Variabel global untuk update otomatis
    private Timer refreshTimer; // Timer untuk auto-refresh
    
    // Warna Tema Modern
    private final Color primaryColor = new Color(52, 152, 219); 
    private final Color successColor = new Color(46, 204, 113); 
    private final Color bgColor = new Color(245, 246, 250);     
    private final Color cardColor = Color.WHITE;

    public CustomerForm() {
        if (Session.id == 0) {
            JOptionPane.showMessageDialog(null, "Sesi habis, silakan login ulang!");
            new LoginForm().setVisible(true);
            this.dispose();
            return;
        }

        setTitle("Premium Pet Shop - " + Session.username);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(bgColor);

        initUI();
        
        // --- INISIALISASI AUTO REFRESH (3 DETIK) ---
        // Memperbarui tampilan saldo belanja secara otomatis
        refreshTimer = new Timer(3000, e -> updateSpendData());
        refreshTimer.start();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // --- HEADER PANEL ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(25, 45, 25, 45)
        ));

        // Kiri: Logo dan Nama
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftHeader.setOpaque(false);
        
        JLabel lblLogo = new JLabel("🐾"); 
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
        
        JLabel lblWelcome = new JLabel("<html><font color='gray' size='4'>Selamat Datang,</font><br/>" 
                                      + "<font color='#2c3e50' size='6'><b>" + Session.username + "</b></font></html>");
        
        leftHeader.add(lblLogo);
        leftHeader.add(lblWelcome);

        // Kanan: Total Belanja
        lblSpend = new JLabel();
        lblSpend.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblSpend.setHorizontalAlignment(SwingConstants.RIGHT);
        updateSpendData(); // Panggilan awal data

        header.add(leftHeader, BorderLayout.WEST);
        header.add(lblSpend, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- MAIN CONTENT AREA ---
        mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);
        add(mainContent, BorderLayout.CENTER);

        showMainMenu();

        // --- FOOTER (LOGOUT AREA) ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        JButton btnLogout = new JButton("🚪 LOGOUT DARI AKUN");
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogout.setForeground(new Color(231, 76, 60)); 
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorder(BorderFactory.createLineBorder(new Color(231, 76, 60), 1));
        btnLogout.setPreferredSize(new Dimension(250, 40));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Apakah Anda yakin ingin logout?", "Konfirmasi Keluar", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                refreshTimer.stop(); 
                userDao.updateLogoutStatus(Session.id); 
                Session.id = 0; 
                new LoginForm().setVisible(true);
                this.dispose();
            }
        });
        
        footer.add(btnLogout);
        add(footer, BorderLayout.SOUTH);
    }

    // Fungsi khusus untuk mengambil data spend terbaru
    private void updateSpendData() {
        // Memanggil DAO untuk menghitung total belanja yang sudah dibayar
        int totalSpend = trxDao.getTotalSpendByUserId(Session.id); 
        lblSpend.setText("<html><right>Total Belanja<br/><font color='#2ecc71'>Rp " 
                        + String.format("%,d", totalSpend) + "</font></right></html>");
    }

    private void showMainMenu() {
        mainContent.removeAll();
        JPanel menuWrapper = new JPanel(new GridBagLayout());
        menuWrapper.setOpaque(false);

        JPanel menuPanel = new JPanel(new GridLayout(1, 3, 35, 0));
        menuPanel.setOpaque(false);
        menuPanel.setPreferredSize(new Dimension(900, 250));

        menuPanel.add(createMenuCard("🛒", "Booking Hewan", "Temukan sahabat barumu", primaryColor, e -> {
            switchPanel(new DaftarHewanForm());
        }));

        menuPanel.add(createMenuCard("🐱", "Koleksi Saya", "Hewan yang kamu miliki", new Color(155, 89, 182), e -> {
            new MyPetsForm().setVisible(true);
        }));

        menuPanel.add(createMenuCard("📋", "Riwayat", "Cek nota transaksi", successColor, e -> {
            new RiwayatPembelianForm().setVisible(true);
        }));

        menuWrapper.add(menuPanel);
        mainContent.add(menuWrapper, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private JPanel createMenuCard(String icon, String title, String desc, Color accent, java.awt.event.ActionListener action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createMatteBorder(0, 0, 5, 0, accent) 
        ));

        JButton btn = new JButton();
        btn.setLayout(new BoxLayout(btn, BoxLayout.Y_AXIS));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.add(Box.createVerticalGlue());
        btn.add(lblIcon);
        btn.add(Box.createRigidArea(new Dimension(0, 15)));
        btn.add(lblTitle);
        btn.add(Box.createRigidArea(new Dimension(0, 5)));
        btn.add(lblDesc);
        btn.add(Box.createVerticalGlue());

        card.add(btn, BorderLayout.CENTER);
        return card;
    }

    private void switchPanel(JPanel panel) {
        mainContent.removeAll();
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navBar.setOpaque(false);
        JButton btnBack = new JButton("⬅ Kembali ke Dashboard");
        btnBack.addActionListener(e -> showMainMenu());
        navBar.add(btnBack);

        mainContent.add(navBar, BorderLayout.NORTH);
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }
}