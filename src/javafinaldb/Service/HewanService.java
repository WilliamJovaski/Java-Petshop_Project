package javafinaldb.Service;

import java.util.List;
import javafinaldb.Dao.HewanDao;
import javafinaldb.Model.Hewan;

public class HewanService {
    private HewanDao hewanDao = new HewanDao();

    public boolean tambahHewan(Hewan h) {
        // FIX: Gunakan getNamaHewan() & Hapus Stok
        if (h.getNamaHewan() == null || h.getNamaHewan().trim().isEmpty()) {
            return false;
        }
        if (h.getHarga() <= 0) return false;
        return hewanDao.insert(h);
    }

    public List<Hewan> getSemuaHewan() {
        return hewanDao.getAll();
    }

    public boolean updateHewan(Hewan h) {
        if (h.getId() <= 0) return false;
        // FIX: Gunakan getNamaHewan()
        if (h.getNamaHewan() == null || h.getNamaHewan().trim().isEmpty()) {
            return false;
        }
        return hewanDao.update(h);
    }

    public boolean hapusHewan(int id) {
        if (id <= 0) return false;
        return hewanDao.delete(id);
    }
}