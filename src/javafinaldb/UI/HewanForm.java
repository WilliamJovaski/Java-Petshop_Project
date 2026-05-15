package javafinaldb.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.sql.Date;
import java.io.File;
import javafinaldb.Dao.HewanDao;
import javafinaldb.Model.Hewan;

public class HewanForm extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private HewanDao hewanDao = new HewanDao();
    
    // Komponen Tambahan untuk Filter
    private JComboBox<String> cbFilterStatus;
    private JTextField txtCariNama;

    public HewanForm() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));
        initUI();
        loadData(); // Memanggil loadData yang sudah mendukung filter
    }

    private void initUI() {
        // --- HEADER SECTION ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Manajemen Individu & Berkas Hewan");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // --- FILTER SECTION (Kanan Atas) ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setOpaque(false);

        txtCariNama = new JTextField(12);
        txtCariNama.setToolTipText("Cari Nama...");
        
        // Pilihan status sesuai data di database
        cbFilterStatus = new JComboBox<>(new String[]{"Semua Status", "Tersedia", "Booking", "Terjual"});
        
        JButton btnRefresh = new JButton("🔄 Refresh");
        
        // Event Listeners untuk Filter
        btnRefresh.addActionListener(e -> {
            txtCariNama.setText("");
            cbFilterStatus.setSelectedIndex(0);
            loadData();
        });
        cbFilterStatus.addActionListener(e -> loadData());
        txtCariNama.addActionListener(e -> loadData()); // Cari saat tekan Enter

        filterPanel.add(new JLabel("Cari:"));
        filterPanel.add(txtCariNama);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(cbFilterStatus);
        filterPanel.add(btnRefresh);
        
        headerPanel.add(filterPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- TABLE SECTION ---
        String[] columns = {"ID", "Kategori", "Jenis", "Nama", "Usia", "Harga", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- ACTION SECTION (Bawah) ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionPanel.setOpaque(false);
        JButton btnAdd = new JButton("+ Tambah Hewan");
        JButton btnEdit = new JButton("✏ Edit Data");
        JButton btnDelete = new JButton("🗑 Hapus");
        
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        add(actionPanel, BorderLayout.SOUTH);

        // Logika Tombol
        btnAdd.addActionListener(e -> showFormDialog(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                Hewan h = hewanDao.getById(id);
                showFormDialog(h);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu!");
            }
        });
        
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Hapus data ID " + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    hewanDao.delete(id);
                    loadData();
                }
            }
        });
    }

    // Method loadData yang telah diperbarui dengan Logika Filter
    private void loadData() {
        model.setRowCount(0);
        List<Hewan> list = hewanDao.getAll();
        
        String filterStatus = cbFilterStatus.getSelectedItem().toString();
        String cariNama = txtCariNama.getText().toLowerCase().trim();

        for (Hewan h : list) {
            // 1. Logika Filter Status
            boolean matchStatus = filterStatus.equals("Semua Status") || h.getStatus().equalsIgnoreCase(filterStatus);
            
            // Handle sinkronisasi 'Booking' dan 'Dibooking'
            if (filterStatus.equals("Booking") && h.getStatus().equalsIgnoreCase("Dibooking")) {
                matchStatus = true;
            }

            // 2. Logika Filter Nama
            boolean matchNama = cariNama.isEmpty() || h.getNamaHewan().toLowerCase().contains(cariNama);

            if (matchStatus && matchNama) {
                model.addRow(new Object[]{
                    h.getId(), 
                    h.getKategori(), 
                    h.getJenisHewan(), 
                    h.getNamaHewan(), 
                    h.getUsia(), // Menampilkan format "X Thn, Y Bln" otomatis
                    "Rp " + String.format("%,d", h.getHarga()), 
                    h.getStatus()
                });
            }
        }
    }

    private void showFormDialog(Hewan existing) {
        boolean isEdit = (existing != null);
        JPanel pnl = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(5, 5, 5, 5);

        // Komponen Input
        JComboBox<String> cbKategori = new JComboBox<>(new String[]{"Kucing", "Anjing", "Burung", "Ular"});
        if (isEdit) cbKategori.setSelectedItem(existing.getKategori());

        JTextField txtJenis = new JTextField(isEdit ? existing.getJenisHewan() : "", 15);
        JTextField txtNama = new JTextField(isEdit ? existing.getNamaHewan() : "", 15);
        JTextField txtTglLahir = new JTextField(isEdit ? existing.getTanggalLahir().toString() : "YYYY-MM-DD", 15);
        JTextField txtHarga = new JTextField(isEdit ? String.valueOf(existing.getHarga()) : "", 15);
        
        JTextField txtPathGambar = new JTextField(isEdit ? existing.getGambar() : "", 10);
        JButton btnPilihGambar = new JButton("📂 Foto");
        
        JTextField txtPathSuara = new JTextField(isEdit ? existing.getFileSuara() : "", 10);
        JButton btnPilihSuara = new JButton("📂 Suara");

        JTextArea txtInfo = new JTextArea(isEdit ? existing.getInfo() : "", 3, 15);

        btnPilihGambar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtPathGambar.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });

        btnPilihSuara.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtPathSuara.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });

        int r = 0;
        addGrid(pnl, new JLabel("Kategori:"), cbKategori, c, r++);
        addGrid(pnl, new JLabel("Jenis (Ras):"), txtJenis, c, r++);
        addGrid(pnl, new JLabel("Nama:"), txtNama, c, r++);
        addGrid(pnl, new JLabel("Tgl Lahir:"), txtTglLahir, c, r++);
        addGrid(pnl, new JLabel("Harga:"), txtHarga, c, r++);
        
        c.gridy = r++; c.gridx = 0; pnl.add(new JLabel("File Gambar:"), c);
        JPanel pnlImg = new JPanel(new BorderLayout());
        pnlImg.add(txtPathGambar, BorderLayout.CENTER); pnlImg.add(btnPilihGambar, BorderLayout.EAST);
        c.gridx = 1; pnl.add(pnlImg, c);

        c.gridy = r++; c.gridx = 0; pnl.add(new JLabel("File Suara:"), c);
        JPanel pnlSnd = new JPanel(new BorderLayout());
        pnlSnd.add(txtPathSuara, BorderLayout.CENTER); pnlSnd.add(btnPilihSuara, BorderLayout.EAST);
        c.gridx = 1; pnl.add(pnlSnd, c);

        addGrid(pnl, new JLabel("Info:"), new JScrollPane(txtInfo), c, r++);

        if (JOptionPane.showConfirmDialog(this, pnl, isEdit ? "Edit Hewan" : "Tambah Hewan", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                Hewan h = isEdit ? existing : new Hewan();
                h.setKategori(cbKategori.getSelectedItem().toString());
                h.setJenisHewan(txtJenis.getText());
                h.setNamaHewan(txtNama.getText());
                h.setTanggalLahir(Date.valueOf(txtTglLahir.getText()));
                h.setHarga(Integer.parseInt(txtHarga.getText()));
                h.setGambar(txtPathGambar.getText());
                h.setFileSuara(txtPathSuara.getText());
                h.setInfo(txtInfo.getText());
                if (!isEdit) h.setStatus("Tersedia");

                if (isEdit) hewanDao.update(h); else hewanDao.insert(h);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal simpan: " + ex.getMessage());
            }
        }
    }

    private void addGrid(JPanel p, JLabel l, JComponent comp, GridBagConstraints c, int row) {
        c.gridy = row; c.gridx = 0; p.add(l, c);
        c.gridx = 1; p.add(comp, c);
    }
}