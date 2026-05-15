package javafinaldb.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafinaldb.DBConnection;
import javafinaldb.Model.Hewan;

public class HewanDao {

    private Hewan mapToHewan(ResultSet rs) throws SQLException {
        Hewan h = new Hewan();
        h.setId(rs.getInt("id"));
        h.setKategori(rs.getString("kategori"));
        h.setJenisHewan(rs.getString("jenis_hewan"));
        h.setNamaHewan(rs.getString("nama_hewan")); // Gunakan setNamaHewan
        h.setTanggalLahir(rs.getDate("tanggal_lahir"));
        h.setHarga(rs.getInt("harga"));
        h.setGambar(rs.getString("gambar"));
        h.setFileSuara(rs.getString("file_suara"));
        h.setInfo(rs.getString("info"));
        h.setStatus(rs.getString("status"));
        return h;
    }

    public List<Hewan> getAll() {
        List<Hewan> list = new ArrayList<>();
        String sql = "SELECT * FROM hewan ORDER BY id DESC";
        try (Connection c = DBConnection.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) list.add(mapToHewan(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // FIX image_787f83.png
    public Hewan getById(int id) {
        String sql = "SELECT * FROM hewan WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapToHewan(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(Hewan h) {
String sql = "INSERT INTO hewan (kategori, nama_hewan, usia, harga, status, gambar, file_suara, info) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, h.getKategori());
            ps.setString(2, h.getJenisHewan());
            ps.setString(3, h.getNamaHewan());
            ps.setDate(4, h.getTanggalLahir());
            ps.setInt(5, h.getHarga());
            ps.setString(6, h.getGambar());
            ps.setString(7, h.getFileSuara());
            ps.setString(8, h.getInfo());
            ps.setString(9, "Tersedia");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(Hewan h) {
        String sql = "UPDATE hewan SET kategori=?, jenis_hewan=?, nama_hewan=?, tanggal_lahir=?, harga=?, gambar=?, file_suara=?, info=?, status=? WHERE id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, h.getKategori());
            ps.setString(2, h.getJenisHewan());
            ps.setString(3, h.getNamaHewan());
            ps.setDate(4, h.getTanggalLahir());
            ps.setInt(5, h.getHarga());
            ps.setString(6, h.getGambar());
            ps.setString(7, h.getFileSuara());
            ps.setString(8, h.getInfo());
            ps.setString(9, h.getStatus());
            ps.setInt(10, h.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // FIX image_788a2e.png
    public boolean delete(int id) {
        String sql = "DELETE FROM hewan WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}