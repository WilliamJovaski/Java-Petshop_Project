package javafinaldb.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import javafinaldb.Dao.HewanDao;
import javafinaldb.Model.Hewan;

public class DaftarHewanForm extends JPanel {
    private JPanel gridPanel;
    private HewanDao hewanDao = new HewanDao();
    private JTextField txtSearch;
    private Timer refreshTimer; 

    public DaftarHewanForm() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 246, 250)); 
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- SECTION ATAS: TITLE & SEARCH ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("🐾 Etalase Pet Shop");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(new Color(44, 62, 80));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Cari 🔍");
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchPanel.add(new JLabel("Cari Jenis/Nama: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- SECTION TENGAH: GRID ETALASE (Scrollable) ---
        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- EVENT LISTENERS ---
        btnSearch.addActionListener(e -> performSearch());

        loadEtalase();

        // Auto Refresh setiap 5 detik untuk memantau update status hewan dari admin
        refreshTimer = new Timer(5000, e -> {
            if (txtSearch.getText().isEmpty()) {
                loadEtalase();
            }
        });
        refreshTimer.start();
    }

    // Memuat data hewan ke dalam grid etalase
    public void loadEtalase() {
        gridPanel.removeAll();
        // Memastikan HewanDao mengambil data lengkap (termasuk kategori & jenis)
        List<Hewan> list = hewanDao.getAll(); 

        for (Hewan h : list) {
            // Hanya hewan yang 'Tersedia' yang muncul di etalase customer
            if ("Tersedia".equals(h.getStatus())) {
                gridPanel.add(createCard(h));
            }
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void performSearch() {
        String keyword = txtSearch.getText().toLowerCase();
        gridPanel.removeAll();
        List<Hewan> list = hewanDao.getAll();

        for (Hewan h : list) {
            // Pencarian berdasarkan Kategori, Jenis, atau Nama Hewan agar data tidak blank
            boolean match = (h.getKategori() != null && h.getKategori().toLowerCase().contains(keyword)) ||
                            (h.getJenisHewan() != null && h.getJenisHewan().toLowerCase().contains(keyword)) ||
                            (h.getNamaHewan() != null && h.getNamaHewan().toLowerCase().contains(keyword));

            if (h.getStatus().equals("Tersedia") && match) {
                gridPanel.add(createCard(h));
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createCard(Hewan h) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // 1. Gambar Hewan
        JLabel lblFoto = new JLabel();
        lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon(h.getGambar());
            Image img = icon.getImage().getScaledInstance(180, 140, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblFoto.setText("[ Gambar Tidak Ada ]");
        }

        // 2. Judul: Kategori - Jenis - Nama (Menampilkan data lengkap agar tidak blank)
        String fullTitle = h.getKategori() + " (" + h.getJenisHewan() + ") - " + h.getNamaHewan();
        JLabel lblNama = new JLabel(fullTitle);
        lblNama.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Harga
        JLabel lblHarga = new JLabel("Rp " + String.format("%,d", h.getHarga()));
        lblHarga.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblHarga.setForeground(new Color(39, 174, 96)); 
        lblHarga.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 4. Usia (Dihitung otomatis dari model Hewan.java menggunakan getUsia())
        JLabel lblUsia = new JLabel("Usia: " + h.getUsia()); 
        lblUsia.setForeground(Color.GRAY);
        lblUsia.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 5. Tombol Lihat Detail
        JButton btnBeli = new JButton("Lihat Detail");
        btnBeli.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBeli.setBackground(new Color(52, 152, 219));
        btnBeli.setForeground(Color.WHITE);
        btnBeli.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnBeli.addActionListener(e -> {
            // Memanggil TransaksiForm yang sudah diperbarui dengan box nama pembeli kecil
            TransaksiForm f = new TransaksiForm(h, this); 
            f.setVisible(true);
            
            f.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    loadEtalase(); 
                }
            });
        });

        card.add(lblFoto);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(lblNama);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblHarga);
        card.add(lblUsia);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(btnBeli);

        return card;
    }
}