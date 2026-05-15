package javafinaldb.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javafinaldb.DBConnection;
import javafinaldb.Logindkk.Session;
import javafinaldb.Model.Hewan;

public class TransaksiForm extends JFrame {

    private Hewan selectedHewan;
    private DaftarHewanForm parent; 
    private JLabel lblNama, lblHarga, lblStatus, lblGambar, lblBatasWaktu;
    private JTextArea txtDeskripsi, txtInfoDetail; 
    private Clip soundClip; 

    // Komponen Admin Onsite
    private JTextField txtNamaPembeli; 
    private JComboBox<String> cbJenis, cbNamaHewan;
    private ArrayList<Hewan> listHewanFilter = new ArrayList<>();

    public TransaksiForm() {
        setTitle("Kasir - Transaksi Onsite");
        setSize(450, 650); // Tinggi ditambah sedikit agar info muat
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
        initUIOnsite();
        loadJenisHewan(); 
    }

    public TransaksiForm(Hewan h, DaftarHewanForm parent) {
        this.selectedHewan = h;
        this.parent = parent;
        setTitle("Detail Hewan & Booking - " + h.getNamaHewan());
        setSize(500, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) { stopSound(); }
        });

        initUI();
    }

    // --- TAMPILAN ADMIN ONSITE ---
    private void initUIOnsite() {
        setLayout(new BorderLayout());
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(20, 30, 20, 30));
        main.setBackground(new Color(245, 246, 250));

        JButton btnBack = new JButton("⬅ Kembali");
        btnBack.addActionListener(e -> this.dispose());
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(btnBack);
        main.add(Box.createRigidArea(new Dimension(0, 20)));

        main.add(new JLabel("Pilih Jenis Hewan:"));
        cbJenis = new JComboBox<>();
        cbJenis.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cbJenis.addActionListener(e -> loadNamaHewan()); 
        main.add(cbJenis);

        main.add(Box.createRigidArea(new Dimension(0, 10)));
        main.add(new JLabel("Pilih Nama Hewan:"));
        cbNamaHewan = new JComboBox<>();
        cbNamaHewan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cbNamaHewan.addActionListener(e -> updateInfoOnsite()); 
        main.add(cbNamaHewan);

        main.add(Box.createRigidArea(new Dimension(0, 10)));
        main.add(new JLabel("Nama Pembeli:"));
        txtNamaPembeli = new JTextField(); 
        txtNamaPembeli.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        main.add(txtNamaPembeli);

        // BAGIAN INFO ONSITE: Menampilkan Jenis & Deskripsi dari DB
        main.add(Box.createRigidArea(new Dimension(0, 10)));
        main.add(new JLabel("Detail Informasi & Deskripsi:"));
        txtInfoDetail = new JTextArea(8, 20); // Ukuran diperbesar agar deskripsi muat
        txtInfoDetail.setEditable(false);
        txtInfoDetail.setLineWrap(true);
        txtInfoDetail.setWrapStyleWord(true);
        txtInfoDetail.setBackground(new Color(230, 230, 230));
        txtInfoDetail.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollInfo = new JScrollPane(txtInfoDetail);
        scrollInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        main.add(scrollInfo);

        main.add(Box.createRigidArea(new Dimension(0, 25)));
        JButton btnProses = new JButton("BAYAR & LUNASKAN");
        btnProses.setBackground(new Color(46, 204, 113));
        btnProses.setForeground(Color.WHITE);
        btnProses.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnProses.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnProses.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnProses.addActionListener(e -> {
            int idx = cbNamaHewan.getSelectedIndex();
            String pembeli = txtNamaPembeli.getText().trim();
            if(idx > 0 && !pembeli.isEmpty()) {
                prosesSimpanDB(listHewanFilter.get(idx-1), pembeli, "Sudah Dibayar", "ONS-", true);
            } else {
                JOptionPane.showMessageDialog(this, "Data belum lengkap!");
            }
        });
        main.add(btnProses);
        add(main, BorderLayout.CENTER);
    }

    private void updateInfoOnsite() {
        int idx = cbNamaHewan.getSelectedIndex();
        if (idx > 0) {
            Hewan h = listHewanFilter.get(idx - 1);
            // Menampilkan Kategori, Jenis, Usia, Harga, dan Deskripsi (Info)
            String info = "Kategori  : " + h.getKategori() + "\n" +
                         "Jenis/Ras : " + h.getJenisHewan() + "\n" +
                         "Nama      : " + h.getNamaHewan() + "\n" +
                         "Usia      : " + h.getUsia() + "\n" +
                         "Harga     : Rp " + String.format("%,d", h.getHarga()) + "\n" +
                         "----------------------------\n" +
                         "Deskripsi : " + h.getInfo(); // Ditarik dari kolom 'info' database
            txtInfoDetail.setText(info);
        } else {
            txtInfoDetail.setText("");
        }
    }

    // --- TAMPILAN CUSTOMER ---
    private void initUI() {
        if (selectedHewan == null) return;
        setLayout(new BorderLayout());
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(20, 30, 20, 30));
        main.setBackground(Color.WHITE);

        JButton btnBack = new JButton("⬅ Kembali ke Daftar");
        btnBack.addActionListener(e -> dispose());
        main.add(btnBack);
        main.add(Box.createRigidArea(new Dimension(0, 10)));

        lblGambar = new JLabel(); lblGambar.setAlignmentX(0.5f);
        updateGambar(); 
        main.add(lblGambar);

        // Header menampilkan Jenis & Nama
        lblNama = new JLabel(selectedHewan.getJenisHewan() + " - " + selectedHewan.getNamaHewan());
        lblNama.setFont(new Font("SansSerif", Font.BOLD, 26)); lblNama.setAlignmentX(0.5f);
        main.add(lblNama);

        lblHarga = new JLabel("Rp " + String.format("%,d", selectedHewan.getHarga()));
        lblHarga.setFont(new Font("SansSerif", Font.BOLD, 22)); lblHarga.setForeground(new Color(46, 204, 113)); lblHarga.setAlignmentX(0.5f);
        main.add(lblHarga);

        lblStatus = new JLabel("Usia: " + selectedHewan.getUsia() + " | Kategori: " + selectedHewan.getKategori()); 
        lblStatus.setAlignmentX(0.5f);
        main.add(lblStatus);

        lblBatasWaktu = new JLabel("⏰ Batas Waktu Bayar: 2 Jam dari sekarang");
        lblBatasWaktu.setForeground(Color.RED);
        lblBatasWaktu.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblBatasWaktu.setAlignmentX(0.5f);
        main.add(Box.createRigidArea(new Dimension(0, 5)));
        main.add(lblBatasWaktu);

        main.add(Box.createRigidArea(new Dimension(0, 10)));
        main.add(new JLabel("Info & Deskripsi:"));
        txtDeskripsi = new JTextArea(selectedHewan.getInfo());
        txtDeskripsi.setLineWrap(true); txtDeskripsi.setWrapStyleWord(true); txtDeskripsi.setEditable(false);
        txtDeskripsi.setBackground(new Color(245, 246, 250));
        JScrollPane scroll = new JScrollPane(txtDeskripsi);
        scroll.setPreferredSize(new Dimension(400, 100)); scroll.setMaximumSize(new Dimension(400, 100));
        main.add(scroll);

        main.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btnSuara = new JButton("🔊 Dengarkan Suara");
        btnSuara.setAlignmentX(0.5f);
        btnSuara.addActionListener(e -> playSound(selectedHewan.getFileSuara()));
        main.add(btnSuara);

        main.add(Box.createRigidArea(new Dimension(0, 20)));
        JButton btnBooking = new JButton("KONFIRMASI BOOKING SEKARANG");
        btnBooking.setBackground(new Color(52, 152, 219)); btnBooking.setForeground(Color.WHITE);
        btnBooking.setFont(new Font("SansSerif", Font.BOLD, 16)); btnBooking.setAlignmentX(0.5f);
        btnBooking.addActionListener(e -> {
            // Konfirmasi ulang sebelum simpan
            String confirmMsg = "Konfirmasi Booking:\n" + 
                                "Hewan: " + selectedHewan.getJenisHewan() + " (" + selectedHewan.getNamaHewan() + ")\n" +
                                "Total: Rp " + String.format("%,d", selectedHewan.getHarga()) + "\n\n" +
                                "Proses sekarang?";
            if (JOptionPane.showConfirmDialog(this, confirmMsg, "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                prosesSimpanDB(selectedHewan, Session.username, "Booking", "BK-", false);
            }
        });
        main.add(btnBooking);

        add(new JScrollPane(main), BorderLayout.CENTER);
    }

    private void prosesSimpanDB(Hewan h, String pembeli, String status, String prefix, boolean isOnsite) {
        String kode = prefix + (System.currentTimeMillis() % 1000000);
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            String expiredValue = isOnsite ? "NULL" : "DATE_ADD(NOW(), INTERVAL 2 HOUR)"; 
            String sqlT = "INSERT INTO transaksi (user_id, hewan_id, total_harga, pembeli, kode_booking, status, tanggal_transaksi, waktu_expired) " +
                          "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, " + expiredValue + ")";
            
            PreparedStatement psT = conn.prepareStatement(sqlT);
            psT.setInt(1, Session.id); psT.setInt(2, h.getId()); psT.setInt(3, h.getHarga());
            psT.setString(4, pembeli); psT.setString(5, kode); psT.setString(6, status);
            psT.executeUpdate();

            String stH = status.equals("Booking") ? "Booking" : "Terjual";
            PreparedStatement psH = conn.prepareStatement("UPDATE hewan SET status = ? WHERE id = ?");
            psH.setString(1, stH); psH.setInt(2, h.getId()); psH.executeUpdate();

            conn.commit();
            
            if (isOnsite) {
                JOptionPane.showMessageDialog(this, "TRANSAKSI ONSITE BERHASIL!\nKode: " + kode);
            } else {
                // Notifikasi Booking diperjelas
                JOptionPane.showMessageDialog(this, "BOOKING BERHASIL!\n" +
                    "Hewan: " + h.getJenisHewan() + " - " + h.getNamaHewan() + "\n" +
                    "Kode Booking: " + kode + "\n" +
                    "Batas Waktu: 2 Jam.");
            }
            
            if (parent != null) parent.loadEtalase();
            dispose();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage()); }
    }

    private void loadJenisHewan() {
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT DISTINCT kategori FROM hewan WHERE status = 'Tersedia'");
            cbJenis.addItem("-- Pilih Jenis --");
            while (rs.next()) cbJenis.addItem(rs.getString("kategori"));
        } catch (Exception e) {}
    }

    private void loadNamaHewan() {
        cbNamaHewan.removeAllItems(); listHewanFilter.clear();
        cbNamaHewan.addItem("-- Pilih Nama --");
        if (cbJenis.getSelectedIndex() <= 0) return;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM hewan WHERE kategori = ? AND status = 'Tersedia'");
            ps.setString(1, cbJenis.getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Hewan h = new Hewan(); 
                h.setId(rs.getInt("id")); 
                h.setKategori(rs.getString("kategori")); // Ambil Kategori
                h.setJenisHewan(rs.getString("jenis_hewan")); // Ambil Jenis/Ras
                h.setNamaHewan(rs.getString("nama_hewan")); 
                h.setHarga(rs.getInt("harga"));
                h.setTanggalLahir(rs.getDate("tanggal_lahir")); 
                h.setInfo(rs.getString("info")); // Ambil Deskripsi
                listHewanFilter.add(h); 
                cbNamaHewan.addItem(h.getNamaHewan());
            }
        } catch (Exception e) {}
    }

    private void updateGambar() {
        try { ImageIcon ic = new ImageIcon(selectedHewan.getGambar()); Image i = ic.getImage().getScaledInstance(280, 220, Image.SCALE_SMOOTH); lblGambar.setIcon(new ImageIcon(i)); } catch (Exception e) {}
    }

    private void playSound(String path) {
        stopSound();
        try { File f = new File(path); if (f.exists()) { AudioInputStream ais = AudioSystem.getAudioInputStream(f); soundClip = AudioSystem.getClip(); soundClip.open(ais); soundClip.start(); } } catch (Exception e) {}
    }

    private void stopSound() { if (soundClip != null && soundClip.isRunning()) { soundClip.stop(); soundClip.close(); } }
}