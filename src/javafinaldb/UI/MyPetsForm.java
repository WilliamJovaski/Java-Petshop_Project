package javafinaldb.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javafinaldb.Dao.TransaksiDao;
import javafinaldb.Logindkk.Session;
import java.io.File;
import javax.sound.sampled.*;

public class MyPetsForm extends JFrame {
    private TransaksiDao trxDao = new TransaksiDao();

    public MyPetsForm() {
        setTitle("Koleksi Hewan Saya 🐾");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 246, 250));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header Section
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JButton btnBack = new JButton("⬅ Dashboard");
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            new CustomerForm().setVisible(true);
            dispose();
        });
        
        JLabel lblTitle = new JLabel("My Animal Collection", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));

        header.add(btnBack, BorderLayout.WEST);
        header.add(lblTitle, BorderLayout.CENTER);
        mainPanel.add(header, BorderLayout.NORTH);

        // Pet Grid Section
        JPanel petGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        petGrid.setBackground(new Color(245, 246, 250));
        
        loadMyPets(petGrid);

        JScrollPane scroll = new JScrollPane(petGrid);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scroll, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadMyPets(JPanel container) {
        // Mengambil data hewan yang sudah dibeli lunas oleh user
        List<Map<String, Object>> pets = trxDao.getMyPets(Session.id);
        
        if (pets == null || pets.isEmpty()) {
            container.setLayout(new FlowLayout(FlowLayout.CENTER));
            JLabel emptyMsg = new JLabel("<html><center>Belum ada koleksi hewan.<br/>Ayo cari hewan impianmu di etalase!</center></html>");
            emptyMsg.setFont(new Font("SansSerif", Font.PLAIN, 16));
            emptyMsg.setForeground(Color.GRAY);
            container.add(emptyMsg);
        } else {
            for (Map<String, Object> p : pets) {
                container.add(createPetCard(p));
            }
        }
    }

    private JPanel createPetCard(Map<String, Object> data) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Gambar Hewan
        JLabel lblImg = new JLabel();
        String path = (String) data.get("gambar");
        if (path != null && new File(path).exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(img));
        } else {
            lblImg.setText("[ No Photo ]");
            lblImg.setPreferredSize(new Dimension(140, 140));
        }
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);

        // Info & Kontrol
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        // Menggabungkan Kategori & Nama
        JLabel name = new JLabel((String) data.get("nama_hewan"));
        name.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        // Menampilkan Info Individu (Jumlah dihilangkan karena per ekor)
        JLabel lblStatus = new JLabel("Status: Terverifikasi");
        lblStatus.setForeground(new Color(46, 204, 113));
        
        // Tombol Putar Suara
        JButton play = new JButton("🔊 Dengarkan Suara");
        play.setCursor(new Cursor(Cursor.HAND_CURSOR));
        play.setAlignmentX(Component.LEFT_ALIGNMENT);
        play.addActionListener(e -> playSound((String) data.get("file_suara")));

        // Tombol Detail Info
        JButton btnInfo = new JButton("ℹ Detail Spesifikasi");
        btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnInfo.addActionListener(e -> {
            String deskripsi = (String) data.get("info");
            if (deskripsi == null || deskripsi.isEmpty()) deskripsi = "Tidak ada informasi tambahan.";
            
            JTextArea textArea = new JTextArea(deskripsi);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.setBackground(new Color(245, 246, 250));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(380, 180));
            JOptionPane.showMessageDialog(this, scrollPane, "Info Detail", JOptionPane.PLAIN_MESSAGE);
        });

        infoPanel.add(name);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblStatus);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(play);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(btnInfo);

        card.add(lblImg, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private void playSound(String path) {
        try {
            if (path == null || path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "File suara tidak tersedia.");
                return;
            }
            File soundFile = new File(path);
            if (!soundFile.exists()) {
                JOptionPane.showMessageDialog(this, "File audio tidak ditemukan.");
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error memutar suara: " + e.getMessage());
        }
    }
}