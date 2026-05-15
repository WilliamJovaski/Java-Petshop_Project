package javafinaldb.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javafinaldb.Dao.TransaksiDao;

public class RiwayatTransaksiForm extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private TransaksiDao trxDao = new TransaksiDao();
    private JTextField txtTglMulai, txtTglSelesai;
    private JComboBox<String> cbFilterJenis;

    public RiwayatTransaksiForm() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- SECTION 1: HEADER & FILTER ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Laporan Transaksi & Monitoring Booking");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlFilter.setOpaque(false);
        
        // Filter Jenis Hewan
        cbFilterJenis = new JComboBox<>(new String[]{"Semua Jenis", "Kucing", "Anjing", "Burung"});
        
        txtTglMulai = new JTextField("2026-01-01", 10);
        txtTglSelesai = new JTextField("2026-12-31", 10);
        
        JButton btnFilter = new JButton("Filter Data 🔍");
        
        pnlFilter.add(new JLabel("Jenis:"));
        pnlFilter.add(cbFilterJenis);
        pnlFilter.add(new JLabel(" Mulai:"));
        pnlFilter.add(txtTglMulai);
        pnlFilter.add(new JLabel(" Sampai:"));
        pnlFilter.add(txtTglSelesai);
        pnlFilter.add(btnFilter);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(pnlFilter, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- SECTION 2: TABEL DENGAN KOLOM LENGKAP ---
        // Penyesuaian Kolom agar sinkron dengan database
        String[] columns = {"ID Trx", "Kode Booking", "Pembeli", "Jenis", "Nama Hewan", "Usia", "Total Harga", "Tanggal", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        table.setRowHeight(35);
        
        // --- LOGIKA PEWARNAAN OTOMATIS ---
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Ambil status dari kolom index 8
                String status = table.getValueAt(row, 8).toString();

                if (status.equalsIgnoreCase("Sudah Dibayar") || status.equalsIgnoreCase("Terjual")) {
                    c.setBackground(new Color(200, 255, 200)); // HIJAU
                } else if (status.equalsIgnoreCase("Booking") || status.equalsIgnoreCase("Menunggu Pembayaran")) {
                    c.setBackground(new Color(255, 230, 150)); // KUNING
                } else if (status.equalsIgnoreCase("Expired")) {
                    c.setBackground(new Color(255, 200, 200)); // MERAH
                } else {
                    c.setBackground(Color.WHITE);
                }

                if (isSelected) {
                    c.setBackground(c.getBackground().darker());
                }
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- SECTION 3: TOMBOL AKSI ---
        JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlAction.setOpaque(false);

        JButton btnValidasi = new JButton("🔑 Validasi Kode Booking");
        btnValidasi.setBackground(new Color(52, 152, 219));
        btnValidasi.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("🗑 Hapus Nota");
        JButton btnRefresh = new JButton("🔄 Semua Data");

        pnlAction.add(btnValidasi);
        pnlAction.add(btnDelete);
        pnlAction.add(btnRefresh);
        add(pnlAction, BorderLayout.SOUTH);

        // --- LISTENERS ---
        btnValidasi.addActionListener(e -> btnProsesBookingAction());
        btnFilter.addActionListener(e -> performFilter());
        btnRefresh.addActionListener(e -> loadAllData());
        btnDelete.addActionListener(e -> hapusTransaksi());

        loadAllData();
    }

    private void btnProsesBookingAction() {
        String inputKode = JOptionPane.showInputDialog(this, "Masukkan Kode Booking Customer:");
        if (inputKode != null && !inputKode.trim().isEmpty()) {
            Map<String, Object> data = trxDao.validasiBooking(inputKode.trim()); 
            if (data == null) {
                JOptionPane.showMessageDialog(this, "KODE TIDAK DITEMUKAN!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String hasil = (String) data.get("hasil_validasi");
                if ("EXPIRED".equals(hasil)) {
                    JOptionPane.showMessageDialog(this, "KODE SUDAH EXPIRED!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                } else {
                    String info = "Pemesan: " + data.get("pembeli") + 
                                 "\nHewan: " + data.get("nama_hewan") + 
                                 "\nTotal: Rp " + String.format("%,d", (int)data.get("total_harga"));
                    int konfirm = JOptionPane.showConfirmDialog(this, info + "\nProses Pembayaran (Lunas)?", "Konfirmasi COD", JOptionPane.YES_NO_OPTION);
                    if (konfirm == JOptionPane.YES_OPTION) {
                        if (trxDao.prosesBayarLunas(inputKode, (int) data.get("hewan_id"))) {
                            JOptionPane.showMessageDialog(this, "Transaksi Berhasil!");
                            loadAllData();
                        }
                    }
                }
            }
        }
    }

    private void loadAllData() {
        model.setRowCount(0);
        // Load data awal dengan rentang sangat luas dan parameter "Semua Jenis"
        List<Map<String, Object>> list = trxDao.getRiwayatLengkap("2000-01-01", "2099-12-31", "Semua Jenis"); 
        for (Map<String, Object> m : list) {
            addRowToModel(m);
        }
    }

    private void performFilter() {
        model.setRowCount(0);
        String mulai = txtTglMulai.getText();
        String selesai = txtTglSelesai.getText();
        String jenis = cbFilterJenis.getSelectedItem().toString();
        
        // Memanggil method DAO dengan filter ganda
        List<Map<String, Object>> list = trxDao.getRiwayatLengkap(mulai, selesai, jenis);
        for (Map<String, Object> m : list) {
            addRowToModel(m);
        }
    }

  private void addRowToModel(Map<String, Object> m) {
        // MENGGUNAKAN KEY YANG SAMA PERSIS DENGAN DAO AGAR DATA MUNCUL
        model.addRow(new Object[]{
            m.get("id"),
            m.get("kode_booking"),
            m.get("pembeli"),
            m.get("key_jenis"), // SINKRON DENGAN DAO
            m.get("key_nama"),  // SINKRON DENGAN DAO
            m.get("key_usia"),  // SINKRON DENGAN DAO
            "Rp " + String.format("%,d", (int)m.get("total_harga")), 
            m.get("tanggal"), 
            m.get("status")
        });
    }
    private void hapusTransaksi() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) model.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "Hapus ID: " + id + "?", "Konfirmasi", 0) == 0) {
                if (trxDao.delete(id)) loadAllData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!");
        }
    }
}