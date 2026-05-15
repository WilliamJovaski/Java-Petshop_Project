package javafinaldb.Model;

/**
 * Model class untuk menampung data Transaksi.
 * Mendukung transaksi online (Customer) dan offline (Admin).
 */
public class Transaksi {
    private int id;
    private int userId;
    private int hewanId;
    private String namaHewan;
    private int jumlah;
    private int totalHarga;
    private String status; 
    private String namaPembeli; // Tambahan: Untuk menampung nama pembeli kustom

    // =====================================================
    // 1. CONSTRUCTOR KOSONG
    // Sangat penting agar Anda bisa membuat objek kosong 
    // sebelum mengisi datanya via setter
    // =====================================================
    public Transaksi() {
    }

    // =====================================================
    // 2. CONSTRUCTOR DENGAN PARAMETER
    // Digunakan saat proses checkout langsung oleh Customer
    // =====================================================
    public Transaksi(int userId, int hewanId, String namaHewan, int jumlah, int totalHarga) {
        this.userId = userId;
        this.hewanId = hewanId;
        this.namaHewan = namaHewan;
        this.jumlah = jumlah;
        this.totalHarga = totalHarga;
        this.status = "Menunggu Pembayaran"; // Status default saat awal beli
    }

    // =====================================================
    // 3. GETTER & SETTER (LENGKAP)
    // Berfungsi untuk akses data dari UI atau DAO
    // =====================================================
    
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }

    public int getUserId() { 
        return userId; 
    }
    
    /**
     * Setter ini sangat penting untuk menghilangkan error merah 
     * pada t.setUserId(0) atau t.setUserId(1) di Form
     */
    public void setUserId(int userId) { 
        this.userId = userId; 
    }

    public int getHewanId() { 
        return hewanId; 
    }
    
    public void setHewanId(int hewanId) { 
        this.hewanId = hewanId; 
    }

    public String getNamaHewan() { 
        return namaHewan; 
    }
    
    public void setNamaHewan(String namaHewan) { 
        this.namaHewan = namaHewan; 
    }

    public int getJumlah() { 
        return jumlah; 
    }
    
    public void setJumlah(int jumlah) { 
        this.jumlah = jumlah; 
    }

    public int getTotalHarga() { 
        return totalHarga; 
    }
    
    public void setTotalHarga(int totalHarga) { 
        this.totalHarga = totalHarga; 
    }

    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }

    // --- TAMBAHAN UNTUK FITUR NAMA PEMBELI KUSTOM ---

    public String getNamaPembeli() {
        return namaPembeli;
    }

    /**
     * Setter ini untuk menghilangkan error "cannot find symbol" 
     * pada t.setNamaPembeli(...) di RiwayatTransaksiForm
     */
    public void setNamaPembeli(String namaPembeli) {
        this.namaPembeli = namaPembeli;
    }
}