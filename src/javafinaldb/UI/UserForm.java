package javafinaldb.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import javafinaldb.Dao.UserDao;
import javafinaldb.Model.User;

public class UserForm extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private UserDao userDao = new UserDao();

    public UserForm() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JLabel lblTitle = new JLabel("Data Pelanggan Terdaftar");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Tabel
        String[] columns = {"ID User", "Username", "Role", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Tombol Hapus (Action Panel)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setOpaque(false);
        
        JButton btnDelete = new JButton("🗑 Hapus Pelanggan Terpilih");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Hapus Pelanggan ID: " + id + "?\n(Catatan: Akan gagal jika user memiliki riwayat transaksi)", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (userDao.deleteUser(id)) { // Pastikan method deleteUser ada di UserDao
                        JOptionPane.showMessageDialog(this, "Berhasil dihapus!");
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Gagal! User ini mungkin memiliki riwayat transaksi yang harus dihapus dulu.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris dulu!");
            }
        });

        actionPanel.add(btnDelete);
        add(actionPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        List<User> list = userDao.getAllUsers();
        for (User u : list) {
            if(u.getRole().equalsIgnoreCase("customer")) {
                model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole(), "Aktif"});
            }
        }
    }

    private void styleTable(JTable t) {
        t.setRowHeight(30);
        t.setSelectionBackground(new Color(232, 244, 253));
    }
}