package javafinaldb.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafinaldb.DBConnection;
import javafinaldb.Model.Transaksi;
import javafinaldb.Logindkk.Session;

public class TransaksiDao {

    // --- 1. FITUR ADMIN: BERSIHKAN BOOKING EXPIRED ---
    public String bersihkanBookingExpired() {
        String daftarHangus = "";
        String sqlGetExpired = "SELECT h.nama_hewan FROM transaksi t JOIN hewan h ON t.hewan_id = h.id WHERE t.status = 'Booking' AND t.waktu_expired < NOW()";
        String sqlDeleteTrx = "DELETE FROM transaksi WHERE status = 'Booking' AND waktu_expired < NOW()";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sqlGetExpired);
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    String nama = rs.getString("nama_hewan");
                    sb.append("- ").append(nama).append("\n");
                    
                    PreparedStatement psH = conn.prepareStatement("UPDATE hewan SET status = 'Tersedia' WHERE nama_hewan = ?");
                    psH.setString(1, nama);
                    psH.executeUpdate();
                }
                daftarHangus = sb.toString();
                st.executeUpdate(sqlDeleteTrx); 
                conn.commit();
            } catch (SQLException ex) { 
                conn.rollback(); 
                ex.printStackTrace(); 
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return daftarHangus;
    }

    // --- 2. FITUR ADMIN: RIWAYAT LENGKAP (DENGAN KATEGORI & JENIS) ---
    public List<Map<String, Object>> getRiwayatLengkap(String mulai, String selesai, String jenis) {
        List<Map<String, Object>> list = new ArrayList<>();
        // Menambahkan h.jenis_hewan agar data muncul lengkap
        String sql = "SELECT t.*, t.pembeli, h.kategori as kategori_hewan, h.jenis_hewan as jenis_db, h.nama_hewan as nama_db, " +
                     "TIMESTAMPDIFF(MONTH, h.tanggal_lahir, CURDATE()) as total_bulan " +
                     "FROM transaksi t LEFT JOIN hewan h ON t.hewan_id = h.id " + 
                     "WHERE DATE(t.tanggal_transaksi) BETWEEN ? AND ? ";
        
        if (jenis != null && !jenis.equals("Semua Jenis")) sql += "AND h.kategori = ? ";
        sql += "ORDER BY t.id DESC";
        
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, mulai); 
            ps.setString(2, selesai);
            if (jenis != null && !jenis.equals("Semua Jenis")) ps.setString(3, jenis);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { 
                list.add(mapRiwayatLengkap(rs)); 
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // --- 3. GET RIWAYAT BY USER ID (UNTUK TABEL CUSTOMER) ---
    public List<Map<String, Object>> getRiwayatByUserId(int userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        // Mengambil Kategori, Jenis, dan Nama Hewan untuk Riwayat Customer
        String sql = (userId == -1) 
            ? "SELECT t.*, t.pembeli, h.kategori as kategori_hewan, h.jenis_hewan as jenis_db, h.nama_hewan as nama_db, TIMESTAMPDIFF(MONTH, h.tanggal_lahir, CURDATE()) as total_bulan FROM transaksi t LEFT JOIN hewan h ON t.hewan_id = h.id ORDER BY t.id DESC"
            : "SELECT t.*, t.pembeli, h.kategori as kategori_hewan, h.jenis_hewan as jenis_db, h.nama_hewan as nama_db, TIMESTAMPDIFF(MONTH, h.tanggal_lahir, CURDATE()) as total_bulan FROM transaksi t LEFT JOIN hewan h ON t.hewan_id = h.id WHERE t.user_id = ? ORDER BY t.id DESC";
        
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (userId != -1) ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { 
                list.add(mapRiwayatLengkap(rs)); 
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // --- 4. FITUR CUSTOMER: FILTER TANGGAL PER USER ---
    public List<Map<String, Object>> getRiwayatByDateAndUser(String mulai, String selesai, int userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT t.*, t.pembeli, h.kategori as kategori_hewan, h.jenis_hewan as jenis_db, h.nama_hewan as nama_db, TIMESTAMPDIFF(MONTH, h.tanggal_lahir, CURDATE()) as total_bulan " +
                     "FROM transaksi t LEFT JOIN hewan h ON t.hewan_id = h.id WHERE t.user_id = ? AND DATE(t.tanggal_transaksi) BETWEEN ? AND ? ORDER BY t.id DESC";
        
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId); 
            ps.setString(2, mulai); 
            ps.setString(3, selesai);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { 
                list.add(mapRiwayatLengkap(rs)); 
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // --- 5. FITUR CUSTOMER: TOTAL BELANJA ---
    public int getTotalSpendByUserId(int userId) {
        String sql = "SELECT SUM(total_harga) as total FROM transaksi WHERE user_id = ? AND status = 'Sudah Dibayar'";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId); 
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return 0;
    }

    // --- 6. KOLEKSI HEWAN SAYA ---
    public List<Map<String, Object>> getMyPets(int userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT h.kategori, h.nama_hewan, h.gambar, h.file_suara, h.info FROM transaksi t LEFT JOIN hewan h ON t.hewan_id = h.id WHERE t.user_id = ? AND t.status = 'Sudah Dibayar'";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId); 
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("nama_hewan", rs.getString("kategori") + " - " + rs.getString("nama_hewan"));
                map.put("gambar", rs.getString("gambar")); 
                map.put("file_suara", rs.getString("file_suara")); 
                map.put("info", rs.getString("info"));
                list.add(map);
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // --- 7. PROSES BOOKING CUSTOMER ---
    public String insertBooking(Transaksi t) {
        String kode = "BK-" + (100000 + new Random().nextInt(900000));
        String sqlTrx = "INSERT INTO transaksi (kode_booking, user_id, hewan_id, total_harga, pembeli, status, tanggal_transaksi, waktu_expired) VALUES (?, ?, ?, ?, ?, 'Booking', CURRENT_TIMESTAMP, DATE_ADD(NOW(), INTERVAL 2 HOUR))";
        
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            PreparedStatement ps1 = c.prepareStatement(sqlTrx);
            ps1.setString(1, kode); 
            ps1.setInt(2, t.getUserId()); 
            ps1.setInt(3, t.getHewanId()); 
            ps1.setInt(4, t.getTotalHarga()); 
            ps1.setString(5, Session.username); 
            ps1.executeUpdate();
            
            PreparedStatement ps2 = c.prepareStatement("UPDATE hewan SET status = 'Booking' WHERE id = ?");
            ps2.setInt(1, t.getHewanId()); 
            ps2.executeUpdate();
            
            c.commit(); 
            return kode;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return null; 
        }
    }

    // --- 7.A PROSES TRANSAKSI ONSITE / PEMBAYARAN LUNAS ---
    public boolean prosesBayarLunas(String kode, int hewanId) {
        String sqlTrx = "UPDATE transaksi SET status = 'Sudah Dibayar', waktu_expired = NULL WHERE kode_booking = ?";
        String sqlHewan = "UPDATE hewan SET status = 'Terjual' WHERE id = ?";
        
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            PreparedStatement ps1 = c.prepareStatement(sqlTrx);
            ps1.setString(1, kode); 
            ps1.executeUpdate();
            
            PreparedStatement ps2 = c.prepareStatement(sqlHewan);
            ps2.setInt(1, hewanId); 
            ps2.executeUpdate();
            
            c.commit(); 
            return true;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // --- 8. VALIDASI BOOKING ---
    public Map<String, Object> validasiBooking(String kode) {
        String sql = "SELECT t.*, h.kategori, h.nama_hewan FROM transaksi t LEFT JOIN hewan h ON t.hewan_id = h.id WHERE t.kode_booking = ? AND t.status = 'Booking'";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, kode); 
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                Timestamp exp = rs.getTimestamp("waktu_expired");
                map.put("hasil_validasi", (exp != null && exp.before(new Timestamp(System.currentTimeMillis()))) ? "EXPIRED" : "VALID");
                map.put("pembeli", rs.getString("pembeli")); 
                map.put("nama_hewan", rs.getString("nama_hewan"));
                map.put("total_harga", rs.getInt("total_harga")); 
                map.put("hewan_id", rs.getInt("hewan_id"));
                return map;
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return null;
    }

    public boolean delete(int id) {
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM transaksi WHERE id = ?")) {
            ps.setInt(1, id); 
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // --- HELPER MAPPING (MENGISI DATA KATEGORI, JENIS, NAMA, & USIA) ---
    private Map<String, Object> mapRiwayatLengkap(ResultSet rs) throws SQLException {
        Map<String, Object> data = new HashMap<>();
        data.put("id", rs.getInt("id"));
        data.put("kode_booking", rs.getString("kode_booking"));
        data.put("pembeli", rs.getString("pembeli"));
        
        // Mengisi Kategori, Jenis, dan Nama dari database agar tidak blank
        data.put("key_kategori", rs.getString("kategori_hewan") == null ? "-" : rs.getString("kategori_hewan"));
        data.put("key_jenis", rs.getString("jenis_db") == null ? "-" : rs.getString("jenis_db"));
        data.put("key_nama", rs.getString("nama_db") == null ? "Data Terhapus" : rs.getString("nama_db"));
        
        // Menghitung Usia Real-time
        if (rs.getObject("total_bulan") != null) {
            int totalBulan = rs.getInt("total_bulan");
            int tahun = totalBulan / 12;
            int bulan = totalBulan % 12;
            data.put("key_usia", tahun + " Thn, " + bulan + " Bln");
        } else {
            data.put("key_usia", "-");
        }
        
        data.put("total_harga", rs.getInt("total_harga"));
        data.put("tanggal", rs.getTimestamp("tanggal_transaksi"));
        data.put("status", rs.getString("status"));
        return data;
    }
}