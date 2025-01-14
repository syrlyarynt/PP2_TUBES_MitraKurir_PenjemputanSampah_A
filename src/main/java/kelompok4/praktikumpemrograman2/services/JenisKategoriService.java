package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.JenisKategori;
import kelompok4.praktikumpemrograman2.model.JenisKategoriMapper;
import kelompok4.praktikumpemrograman2.utils.DataCache;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class JenisKategoriService {
    private final JenisKategoriMapper mapper;
    private final SqlSession sqlSession;
    private static final String CACHE_KEY_ALL = "kategori_all";
    private static final String CACHE_KEY_PREFIX = "kategori_";

    public JenisKategoriService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(JenisKategoriMapper.class);
        this.sqlSession = sqlSession;
    }

    public List<JenisKategori> getAllKategori() {
        // Cek cache dulu
        Optional<Object> cached = DataCache.get(CACHE_KEY_ALL);
        if (cached.isPresent()) {
            return (List<JenisKategori>) cached.get();
        }

        // Jika tidak ada di cache, ambil dari database
        List<JenisKategori> kategoriList = mapper.getAllKategori();

        // Simpan ke cache
        DataCache.put(CACHE_KEY_ALL, kategoriList);

        return kategoriList;
    }

    public JenisKategori getKategoriById(int id) {
        // Cek cache untuk id spesifik
        String cacheKey = CACHE_KEY_PREFIX + id;
        Optional<Object> cached = DataCache.get(cacheKey);
        if (cached.isPresent()) {
            return (JenisKategori) cached.get();
        }

        // Jika tidak ada di cache, ambil dari database
        JenisKategori kategori = mapper.getKategoriById(id);

        // Simpan ke cache jika ditemukan
        if (kategori != null) {
            DataCache.put(cacheKey, kategori);
        }

        return kategori;
    }

    public void createKategori(JenisKategori kategori) {
        mapper.insertKategori(kategori);
        sqlSession.commit();

        // Invalidate cache karena ada data baru
        invalidateCache();
    }

    public void updateKategori(JenisKategori kategori) {
        mapper.updateKategori(kategori);
        sqlSession.commit();

        // Invalidate cache karena ada perubahan data
        invalidateCache();
        // Update cache untuk id spesifik
        DataCache.put(CACHE_KEY_PREFIX + kategori.getId(), kategori);
    }

    public void deleteKategori(int id) {
        mapper.deleteKategori(id);
        sqlSession.commit();

        // Invalidate cache karena ada penghapusan data
        invalidateCache();
    }

    private void invalidateCache() {
        // Hapus cache untuk daftar semua kategori
        DataCache.clear();
    }
}