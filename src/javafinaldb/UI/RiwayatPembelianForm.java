package javafinaldb.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javafinaldb.Dao.TransaksiDao;
import javafinaldb.Logindkk.Session;

public class RiwayatPembelianForm extends JFrame {
    private TransaksiDao trxDao = new TransaksiDao();
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotalSpend;
    private JTextField txtTglMulai, txtTglSelesai; 
    private List<Map<String, Object>> dataRiwayat; 

    public RiwayatPembelianForm() {
        setTitle("Riwayat Pembelian Saya - " + Session.username);
        setSize(1100, 600); // Lebar ditambah untuk kolom baru
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // --- HEADER & FILTER ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Nota Pembelian & Riwayat", SwingConstants.LEFT);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlFilter.setOpaque(false);
        txtTglMulai = new JTextField("2026-01-01", 10);
        txtTglSelesai = new JTextField("2026-12-31", 10);
        JButton btnFilter = new JButton("Cari Tanggal 🔍");
        
        pnlFilter.add(new JLabel("Dari:"));
        pnlFilter.add(txtTglMulai);
        pnlFilter.add(new JLabel(" Sampai:"));
        pnlFilter.add(txtTglSelesai);
        pnlFilter.add(btnFilter);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(pnlFilter, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- TABEL: DITAMBAH KOLOM JENIS & USIA ---
        String[] columns = {"ID Trx", "Kode Booking", "Kategori (Jenis)", "Nama Hewan", "Usia", "Total Harga", "Tanggal", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        table.setRowHeight(35);
        
        // --- LOGIKA PEWARNAAN BARIS ---
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Index status berubah ke 7 karena penambahan kolom
                Object statusObj = table.getValueAt(row, 7); 
                String status = (statusObj != null) ? statusObj.toString() : "";

                if ("Sudah Dibayar".equalsIgnoreCase(status)) {
                    c.setBackground(new Color(200, 255, 200)); // HIJAU: Lunas
                    c.setForeground(Color.BLACK);
                } else if ("Booking".equalsIgnoreCase(status) || "Dibooking".equalsIgnoreCase(status)) {
                    c.setBackground(new Color(255, 230, 150)); // ORANYE: Booking
                    c.setForeground(Color.BLACK);
                } else if ("Expired".equalsIgnoreCase(status)) {
                    c.setBackground(new Color(255, 200, 200)); // MERAH: Kadaluarsa
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                if (isSelected) {
                    c.setBackground(c.getBackground().darker());
                }
                return c;
            }
        });

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        
        lblTotalSpend = new JLabel("Total Belanja Lunas: Rp 0");
        lblTotalSpend.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTotalSpend.setForeground(new Color(39, 174, 96)); 
        
        JButton btnRefresh = new JButton("Tampilkan Semua");
        btnRefresh.addActionListener(e -> loadData());
        
        footerPanel.add(lblTotalSpend, BorderLayout.WEST);
        footerPanel.add(btnRefresh, BorderLayout.EAST);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        btnFilter.addActionListener(e -> performFilter());
    }

    private void loadData() {
        dataRiwayat = trxDao.getRiwayatByUserId(Session.id);
        updateTable(dataRiwayat);
    }

    private void performFilter() {
        String mulai = txtTglMulai.getText();
        String selesai = txtTglSelesai.getText();
        dataRiwayat = trxDao.getRiwayatByDateAndUser(mulai, selesai, Session.id);
        updateTable(dataRiwayat);
    }

    private void updateTable(List<Map<String, Object>> list) {
        model.setRowCount(0);
        long totalLunas = 0;

        for (Map<String, Object> r : list) {
            String status = (String) r.get("status");
            int harga = (int) r.get("total_harga");

            // Menggunakan key mapping yang sesuai dengan TransaksiDao terbaru
            model.addRow(new Object[]{
                r.get("id"),
                r.get("kode_booking"),
                r.get("key_kategori") + " (" + r.get("key_jenis") + ")", // Menampilkan Kategori & Jenis
                r.get("key_nama"), // Menampilkan Nama Hewan (Vanessa, Luna, dll)
                r.get("key_usia"), // Menampilkan Usia otomatis (Thn, Bln)
                "Rp " + String.format("%,d", harga),
                r.get("tanggal"),
                status 
            });

            if ("Sudah Dibayar".equals(status)) {
                totalLunas += harga;
            }
        }
        lblTotalSpend.setText("Total Belanja Lunas: Rp " + String.format("%,d", totalLunas));
    }
}