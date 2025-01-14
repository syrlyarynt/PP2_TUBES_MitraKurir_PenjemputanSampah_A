//package kelompok4.praktikumpemrograman2.services;
//
//import kelompok4.praktikumpemrograman2.model.JenisKategori;
//import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
//import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputan;
//import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputanMapper;
//import org.apache.ibatis.exceptions.PersistenceException;
//import org.apache.ibatis.session.SqlSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//
//public class PermintaanPenjemputanService {
//    private static final Logger logger = LoggerFactory.getLogger(PermintaanPenjemputanService.class);
//    private final PermintaanPenjemputanMapper mapper;
//    private final LokasiDropboxService dropboxService;
//    private final SqlSession sqlSession;
//
//    public PermintaanPenjemputanService(SqlSession sqlSession) {
//        logger.debug("Initializing PermintaanPenjemputanService with SqlSession: {}", sqlSession);
//        this.sqlSession = sqlSession;
//        this.mapper = sqlSession.getMapper(PermintaanPenjemputanMapper.class);
//        this.dropboxService = new LokasiDropboxService(sqlSession);
//    }
//
//    public void createPermintaan(PermintaanPenjemputan permintaan) {
//        try {
//            logger.info("Mencoba insert permintaan: {}", permintaan);
//            mapper.insertPermintaan(permintaan);
//            sqlSession.commit();
//            logger.info("Permintaan berhasil dibuat dengan ID: {}", permintaan.getIdPermintaan());
//        } catch (Exception e) {
//            logger.error("Gagal membuat permintaan: {}", e.getMessage(), e);
//            sqlSession.rollback();
//            throw new RuntimeException("Insert gagal: " + e.getMessage());
//        }
//    }
//
//    public boolean isKategoriExists(int id) {
//        return mapper.isKategoriExists(id) > 0;
//    }
//
//    public boolean isDropboxExists(int id) {
//        return mapper.isDropboxExists(id) > 0;
//    }
//
//
//
//    public List<PermintaanPenjemputan> getAllPermintaan() {
//        System.out.println("=== PermintaanPenjemputanController.getAllPermintaan START ===");
//        sqlSession.clearCache();
//        List<PermintaanPenjemputan> permintaanList = mapper.getAllPermintaan();
//
//        if (permintaanList == null || permintaanList.isEmpty()) {
//            System.out.println("Tidak ada data permintaan ditemukan!");
//        } else {
//            System.out.println("Retrieved " + permintaanList.size() + " permintaan records.");
//            for (PermintaanPenjemputan permintaan : permintaanList) {
//                System.out.println("Permintaan ID: " + permintaan.getIdPermintaan() + ", Nama Pelanggan: " + permintaan.getNamaPelanggan() + ", Alamat: " + permintaan.getAlamat());
//            }
//        }
//
//        System.out.println("=== PermintaanPenjemputanController.getAllPermintaan END ===");
//        return permintaanList;
//    }
//
//
//    public PermintaanPenjemputan getPermintaanById(int id) {
//        logger.debug("Fetching permintaan with ID: {}", id);
//        PermintaanPenjemputan permintaan = mapper.getPermintaanById(id);
//        if (permintaan == null) {
//            logger.warn("No permintaan found with ID: {}", id);
//        }
//        return permintaan;
//    }
//
//    public void updatePermintaan(PermintaanPenjemputan permintaan) {
//        logger.info("Attempting to update permintaan: {}", permintaan);
//        try {
//            mapper.updatePermintaan(permintaan);
//            sqlSession.commit();
//            logger.info("Successfully updated permintaan with ID: {}", permintaan.getIdPermintaan());
//        } catch (Exception e) {
//            logger.error("Failed to update permintaan: {}", permintaan, e);
//            sqlSession.rollback();
//            throw e;
//        }
//    }
//
//    public void deletePermintaan(int id) {
//        logger.info("Attempting to delete permintaan with ID: {}", id);
//        try {
//            mapper.deletePermintaan(id);
//            sqlSession.commit();
//            logger.info("Successfully deleted permintaan with ID: {}", id);
//        } catch (Exception e) {
//            logger.error("Failed to delete permintaan with ID: {}", id, e);
//            sqlSession.rollback();
//            throw e;
//        }
//    }
//
//    public List<JenisKategori> getAllKategoriSampah() {
//        logger.debug("Fetching all kategori sampah");
//        List<JenisKategori> kategoriList = mapper.getAllKategoriSampah();
//        logger.debug("Retrieved {} kategori records", kategoriList.size());
//        return kategoriList;
//    }
//
//    public List<LokasiDropbox> getAllDropbox() {
//        logger.debug("Fetching all dropbox locations");
//        List<LokasiDropbox> dropboxList = dropboxService.getAllDropbox();
//        logger.debug("Retrieved {} dropbox records", dropboxList.size());
//        return dropboxList;
//    }
//}

package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.*;
import kelompok4.praktikumpemrograman2.utils.DataCache;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

public class PermintaanPenjemputanService {
    private static final Logger logger = LoggerFactory.getLogger(PermintaanPenjemputanService.class);
    private static final String CACHE_KEY_ALL = "permintaan_all";
    private static final String CACHE_KEY_PREFIX = "permintaan_";
    private static final String CACHE_KEY_KATEGORI = "kategori_all";
    private static final String CACHE_KEY_DROPBOX = "dropbox_all";

    private final PermintaanPenjemputanMapper mapper;
    private final LokasiDropboxService dropboxService;
    private final SqlSession sqlSession;

    public PermintaanPenjemputanService(SqlSession sqlSession) {
        logger.debug("Initializing PermintaanPenjemputanService with SqlSession: {}", sqlSession);
        this.sqlSession = sqlSession;
        this.mapper = sqlSession.getMapper(PermintaanPenjemputanMapper.class);
        this.dropboxService = new LokasiDropboxService(sqlSession);
    }

    public void createPermintaan(PermintaanPenjemputan permintaan) {
        try {
            logger.info("Mencoba insert permintaan: {}", permintaan);
            mapper.insertPermintaan(permintaan);
            sqlSession.commit();
            logger.info("Permintaan berhasil dibuat dengan ID: {}", permintaan.getIdPermintaan());
            invalidateCache();
        } catch (Exception e) {
            logger.error("Gagal membuat permintaan: {}", e.getMessage(), e);
            sqlSession.rollback();
            throw new RuntimeException("Insert gagal: " + e.getMessage());
        }
    }

    public List<PermintaanPenjemputan> getAllPermintaan() {
        Optional<Object> cached = DataCache.get(CACHE_KEY_ALL);
        if (cached.isPresent()) {
            return (List<PermintaanPenjemputan>) cached.get();
        }

        System.out.println("=== PermintaanPenjemputanController.getAllPermintaan START ===");
        List<PermintaanPenjemputan> permintaanList = mapper.getAllPermintaan();

        if (permintaanList != null && !permintaanList.isEmpty()) {
            DataCache.put(CACHE_KEY_ALL, permintaanList);
            System.out.println("Retrieved " + permintaanList.size() + " permintaan records.");
            for (PermintaanPenjemputan permintaan : permintaanList) {
                System.out.println("Permintaan ID: " + permintaan.getIdPermintaan() +
                        ", Nama Pelanggan: " + permintaan.getNamaPelanggan() +
                        ", Alamat: " + permintaan.getAlamat());
            }
        } else {
            System.out.println("Tidak ada data permintaan ditemukan!");
        }

        System.out.println("=== PermintaanPenjemputanController.getAllPermintaan END ===");
        return permintaanList;
    }

    public PermintaanPenjemputan getPermintaanById(int id) {
        String cacheKey = CACHE_KEY_PREFIX + id;
        Optional<Object> cached = DataCache.get(cacheKey);
        if (cached.isPresent()) {
            return (PermintaanPenjemputan) cached.get();
        }

        logger.debug("Fetching permintaan with ID: {}", id);
        PermintaanPenjemputan permintaan = mapper.getPermintaanById(id);
        if (permintaan != null) {
            DataCache.put(cacheKey, permintaan);
        } else {
            logger.warn("No permintaan found with ID: {}", id);
        }
        return permintaan;
    }

    public void updatePermintaan(PermintaanPenjemputan permintaan) {
        logger.info("Attempting to update permintaan: {}", permintaan);
        try {
            mapper.updatePermintaan(permintaan);
            sqlSession.commit();
            logger.info("Successfully updated permintaan with ID: {}", permintaan.getIdPermintaan());
            invalidateCache();
            DataCache.put(CACHE_KEY_PREFIX + permintaan.getIdPermintaan(), permintaan);
        } catch (Exception e) {
            logger.error("Failed to update permintaan: {}", permintaan, e);
            sqlSession.rollback();
            throw e;
        }
    }

    public void deletePermintaan(int id) {
        logger.info("Attempting to delete permintaan with ID: {}", id);
        try {
            mapper.deletePermintaan(id);
            sqlSession.commit();
            logger.info("Successfully deleted permintaan with ID: {}", id);
            invalidateCache();
        } catch (Exception e) {
            logger.error("Failed to delete permintaan with ID: {}", id, e);
            sqlSession.rollback();
            throw e;
        }
    }

    public List<JenisKategori> getAllKategoriSampah() {
        Optional<Object> cached = DataCache.get(CACHE_KEY_KATEGORI);
        if (cached.isPresent()) {
            return (List<JenisKategori>) cached.get();
        }

        logger.debug("Fetching all kategori sampah");
        List<JenisKategori> kategoriList = mapper.getAllKategoriSampah();
        logger.debug("Retrieved {} kategori records", kategoriList.size());
        DataCache.put(CACHE_KEY_KATEGORI, kategoriList);
        return kategoriList;
    }

    public List<LokasiDropbox> getAllDropbox() {
        Optional<Object> cached = DataCache.get(CACHE_KEY_DROPBOX);
        if (cached.isPresent()) {
            return (List<LokasiDropbox>) cached.get();
        }

        logger.debug("Fetching all dropbox locations");
        List<LokasiDropbox> dropboxList = dropboxService.getAllDropbox();
        logger.debug("Retrieved {} dropbox records", dropboxList.size());
        DataCache.put(CACHE_KEY_DROPBOX, dropboxList);
        return dropboxList;
    }

    public boolean isKategoriExists(int id) {
        return mapper.isKategoriExists(id) > 0;
    }

    public boolean isDropboxExists(int id) {
        return mapper.isDropboxExists(id) > 0;
    }

    private void invalidateCache() {
        DataCache.invalidateKeysByPrefix(CACHE_KEY_PREFIX);
        DataCache.remove(CACHE_KEY_ALL);
        DataCache.remove(CACHE_KEY_KATEGORI);
        DataCache.remove(CACHE_KEY_DROPBOX);
    }
}