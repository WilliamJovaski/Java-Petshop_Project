package javafinaldb.Model;

import java.time.LocalDate;
import java.time.Period;
import java.sql.Date;

/**
 * Model class untuk individu hewan unik.
 * Menggantikan sistem stok menjadi sistem per-ekor.
 */
public class Hewan {
    private int id;
    private String kategori;    // Kucing, Anjing, Burung, Ular
    private String jenisHewan;  // Contoh: Anggora, Persa, Golden Retriever
    private String namaHewan;   // Nama panggilan individu
    private Date tanggalLahir;  // Digunakan untuk hitung usia otomatis
    private int harga;
    private String gambar;      
    private String fileSuara;   
    private String info;        
    private String status;      // Tersedia, Dibooking, Terjual

    public Hewan() {
    }

    // Constructor Full untuk sistem baru
    public Hewan(int id, String kategori, String jenisHewan, String namaHewan, Date tanggalLahir, int harga, String gambar, String fileSuara, String info, String status) {
        this.id = id;
        this.kategori = kategori;
        this.jenisHewan = jenisHewan;
        this.namaHewan = namaHewan;
        this.tanggalLahir = tanggalLahir;
        this.harga = harga;
        this.gambar = gambar;
        this.fileSuara = fileSuara;
        this.info = info;
        this.status = status;
    }

    // =====================================================
    // LOGIKA USIA OTOMATIS (Hitung dari Tanggal Lahir)
    // =====================================================
    public String getUsia() {
        if (this.tanggalLahir == null) return "Data tgl lahir kosong";
        
        LocalDate lahir = this.tanggalLahir.toLocalDate();
        LocalDate sekarang = LocalDate.now();
        Period selisih = Period.between(lahir, sekarang);
        
        return selisih.getYears() + " Thn, " + selisih.getMonths() + " Bln";
    }

    // =====================================================
    // GETTER & SETTER
    // =====================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getJenisHewan() { return jenisHewan; }
    public void setJenisHewan(String jenisHewan) { this.jenisHewan = jenisHewan; }

    public String getNamaHewan() { return namaHewan; }
    public void setNamaHewan(String namaHewan) { this.namaHewan = namaHewan; }

    public Date getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(Date tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public int getHarga() { return harga; }
    public void setHarga(int harga) { this.harga = harga; }

    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }

    public String getFileSuara() { return fileSuara; }
    public void setFileSuara(String fileSuara) { this.fileSuara = fileSuara; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Alias untuk menghindari error pada form yang memanggil getDeskripsi
    public String getDeskripsi() { return this.info; }

    @Override
    public String toString() {
        return this.jenisHewan + " - " + this.namaHewan;
    }
}