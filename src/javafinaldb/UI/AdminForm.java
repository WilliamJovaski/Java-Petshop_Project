package javafinaldb.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafinaldb.DBConnection;
import javafinaldb.Dao.TransaksiDao;
import javafinaldb.Dao.UserDao;
import javafinaldb.Logindkk.Session;

public class AdminForm extends JFrame {
    private JPanel mainContent;
    private Timer refreshTimer;
    private UserDao userDao = new UserDao();
    private TransaksiDao trxDao = new TransaksiDao();

    // Tema Warna Modern
    private final Color primaryColor = new Color(52, 152, 219); 
    private final Color successColor = new Color(46, 204, 113); 
    private final Color warningColor = new Color(241, 196, 15); 
    private final Color dangerColor = new Color(231, 76, 60);   
    private final Color bgColor = new Color(245, 246, 250);     
    private final Color cardColor = Color.WHITE;

    public AdminForm() {
        // 1. Validasi Sesi Login
        if (Session.id == 0) {
            JOptionPane.showMessageDialog(null, "Sesi habis, silakan login!");
            SwingUtilities.invokeLater(() -> {
                new LoginForm().setVisible(true);
            });
            this.dispose();
            return;
        }

        setTitle("Pet Shop Central Management - " + Session.username);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(bgColor);

        initUI();

        // 2. Inisialisasi Timer (Update Dashboard & Cek Expired otomatis tiap 5 detik)
        refreshTimer = new Timer(5000, e -> {
            // FITUR OTOMATIS: Bersihkan booking yang sudah lewat 2 jam
            String daftarHangus = trxDao.bersihkanBookingExpired(); 
            
            // Jika ada hewan yang hangus, munculkan notifikasi pop-up
            if (daftarHangus != null && !daftarHangus.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "NOTIFIKASI SISTEM:\nBooking untuk hewan berikut telah EXPIRED:\n" + daftarHangus + 
                    "\nHewan otomatis kembali tersedia di etalase.", 
                    "Booking Expired", JOptionPane.INFORMATION_MESSAGE);
            }

            // Hanya refresh dashboard jika sedang tidak membuka menu lain agar tidak tertutup otomatis
            if (mainContent.getComponentCount() > 0 && 
                !(mainContent.getLayout() instanceof BorderLayout && ((BorderLayout)mainContent.getLayout()).getLayoutComponent(BorderLayout.NORTH) != null)) {
                showDashboard();
            }
        });
        refreshTimer.start();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(20, 45, 20, 45)
        ));

        JLabel lblWelcome = new JLabel("<html><font color='gray' size='4'>Panel Administrator,</font><br/>" 
                + "<font color='#2c3e50' size='6'><b>" + Session.username + "</b></font></html>");
        
        JButton btnLogout = new JButton("🚪 LOGOUT");
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogout.setForeground(dangerColor);
        btnLogout.setFocusPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> logoutAction());

        header.add(lblWelcome, BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- CONTENT AREA ---
        mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);
        add(mainContent, BorderLayout.CENTER);

        showDashboard(); 
    }

    private void showDashboard() {
        mainContent.removeAll();
        
        JPanel dashboardWrapper = new JPanel();
        dashboardWrapper.setLayout(new BoxLayout(dashboardWrapper, BoxLayout.Y_AXIS));
        dashboardWrapper.setOpaque(false);
        dashboardWrapper.setBorder(new EmptyBorder(30, 50, 30, 50));

        // 1. Ambil Data Real-time dari Database
        int pendapatan = 0, trxCount = 0, userCount = 0;
        try (Connection c = DBConnection.getConnection()) {
            // Mengambil sum pendapatan dari transaksi lunas
            String sqlPendapatan = "SELECT SUM(total_harga) FROM transaksi WHERE status IN ('Sudah Dibayar', 'Terjual')";
            ResultSet rs1 = c.createStatement().executeQuery(sqlPendapatan);
            if (rs1.next()) pendapatan = rs1.getInt(1); 

            ResultSet rsTrx = c.createStatement().executeQuery("SELECT COUNT(id) FROM transaksi");
            if (rsTrx.next()) trxCount = rsTrx.getInt(1);

            ResultSet rs3 = c.createStatement().executeQuery("SELECT COUNT(id) FROM users WHERE role='customer'");
            if (rs3.next()) userCount = rs3.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }

        // 2. Panel Statistik (Atas)
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        statsPanel.add(createStatBox("💰 Pendapatan (Lunas)", "Rp " + String.format("%,d", pendapatan), successColor));
        statsPanel.add(createStatBox("🛒 Total Transaksi", trxCount + " Nota", primaryColor));
        statsPanel.add(createStatBox("👥 Pelanggan", userCount + " Orang", new Color(155, 89, 182)));

        // 3. Panel Menu Utama (Tengah)
        JPanel menuPanel = new JPanel(new GridLayout(1, 4, 25, 0));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(40, 0, 0, 0));

        // Membuka menu kelola hewan
        menuPanel.add(createMenuCard("📦", "Kelola Hewan", "Input Jenis & Kategori", primaryColor, e -> switchPanel(new HewanForm())));
        menuPanel.add(createMenuCard("🔑", "Proses Booking", "Validasi Kode & COD", dangerColor, e -> btnProsesBookingAction()));
        menuPanel.add(createMenuCard("👥", "Data User", "Manajemen Pelanggan", warningColor, e -> switchPanel(new UserForm())));
        menuPanel.add(createMenuCard("📜", "Riwayat", "Log Transaksi Toko", successColor, e -> switchPanel(new RiwayatTransaksiForm())));

        // 4. Panel Khusus (Bawah) - Transaksi Onsite
        JPanel onsitePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        onsitePanel.setOpaque(false);
        onsitePanel.setBorder(new EmptyBorder(30,0,0,0));
        onsitePanel.add(createMenuCard("🛒", "Transaksi Onsite", "Input Kasir Langsung", successColor, e -> {
            refreshTimer.stop();
            TransaksiForm tForm = new TransaksiForm();
            tForm.setVisible(true);
            tForm.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent windowEvent) {
                    showDashboard();
                    refreshTimer.start();
                }
            });
        }));

        dashboardWrapper.add(statsPanel);
        dashboardWrapper.add(menuPanel);
        dashboardWrapper.add(onsitePanel);
        
        mainContent.add(dashboardWrapper, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void btnProsesBookingAction() {
        // Dialog input kode booking
        String inputKode = JOptionPane.showInputDialog(this, "Masukkan Kode Booking Customer:");
        if (inputKode != null && !inputKode.trim().isEmpty()) {
            Map<String, Object> data = trxDao.validasiBooking(inputKode.trim()); 
            if (data == null) {
                JOptionPane.showMessageDialog(this, "KODE TIDAK DITEMUKAN!", "Gagal", JOptionPane.ERROR_MESSAGE);
            } else if ("EXPIRED".equals(data.get("hasil_validasi"))) {
                // Notifikasi jika kode sudah kedaluwarsa
                JOptionPane.showMessageDialog(this, "KODE SUDAH KEDALUWARSA (LEBIH DARI 2 JAM)!", "Gagal", JOptionPane.WARNING_MESSAGE);
            } else {
                // Tampilkan info detail booking sebelum pelunasan
                String info = "Pemesan : " + data.get("pembeli") + 
                             "\nHewan : " + data.get("nama_hewan") + 
                             "\nTotal : Rp " + String.format("%,d", (int)data.get("total_harga"));
                int konfirm = JOptionPane.showConfirmDialog(this, info + "\nSelesaikan Pembayaran?", "Konfirmasi COD", JOptionPane.YES_NO_OPTION);
                if (konfirm == JOptionPane.YES_OPTION) {
                    // Update status transaksi menjadi 'Sudah Dibayar'
                    if (trxDao.prosesBayarLunas(inputKode.trim(), (int) data.get("hewan_id"))) {
                        JOptionPane.showMessageDialog(this, "Pembayaran Berhasil!");
                        showDashboard(); 
                    }
                }
            }
        }
    }

    private void switchPanel(JPanel panel) {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }

        mainContent.removeAll();
        // Navigasi Bar Atas untuk kembali
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        nav.setOpaque(false);
        nav.setBorder(new EmptyBorder(10, 45, 0, 0));
        
        JButton b = new JButton("⬅ Kembali ke Dashboard");
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> {
            mainContent.removeAll();
            showDashboard();
            if (refreshTimer != null) refreshTimer.start();
        }); 
        
        nav.add(b);
        mainContent.add(nav, BorderLayout.NORTH);
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate(); 
        mainContent.repaint();
    }

    private JPanel createStatBox(String title, String value, Color color) {
        JPanel box = new JPanel(new GridLayout(2, 1));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createMatteBorder(0, 5, 0, 0, color)
        ));
        JLabel t = new JLabel("  " + title); t.setForeground(Color.GRAY);
        JLabel v = new JLabel("  " + value); v.setFont(new Font("SansSerif", Font.BOLD, 18));
        box.add(t); box.add(v);
        return box;
    }

    private JPanel createMenuCard(String icon, String title, String desc, Color accent, java.awt.event.ActionListener action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(cardColor);
        card.setPreferredSize(new Dimension(220, 160));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createMatteBorder(0, 0, 4, 0, accent)
        ));
        
        JButton btn = new JButton();
        btn.setLayout(new BoxLayout(btn, BoxLayout.Y_AXIS));
        btn.setContentAreaFilled(false); 
        btn.setBorderPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);

        JLabel i = new JLabel(icon); i.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40)); i.setAlignmentX(0.5f);
        JLabel t = new JLabel(title); t.setFont(new Font("SansSerif", Font.BOLD, 18)); t.setAlignmentX(0.5f);
        JLabel d = new JLabel(desc); d.setForeground(Color.GRAY); d.setAlignmentX(0.5f);

        btn.add(Box.createVerticalGlue()); 
        btn.add(i); 
        btn.add(Box.createRigidArea(new Dimension(0, 10)));
        btn.add(t); 
        btn.add(d); 
        btn.add(Box.createVerticalGlue());
        
        card.add(btn, BorderLayout.CENTER);
        return card;
    }

    private void logoutAction() {
        if (JOptionPane.showConfirmDialog(this, "Yakin Logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == 0) {
            if (refreshTimer != null) refreshTimer.stop();
            userDao.updateLogoutStatus(Session.id);
            Session.logout();
            new LoginForm().setVisible(true);
            this.dispose();
        }
    }
}