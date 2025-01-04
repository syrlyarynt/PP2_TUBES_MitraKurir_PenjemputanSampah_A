package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.JenisKategori;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputan;
import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputanMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PermintaanPenjemputanService {
    private static final Logger logger = LoggerFactory.getLogger(PermintaanPenjemputanService.class);
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
        } catch (Exception e) {
            logger.error("Gagal membuat permintaan: {}", e.getMessage(), e);
            sqlSession.rollback();
            throw new RuntimeException("Insert gagal: " + e.getMessage());
        }
    }

    public boolean isKategoriExists(int id) {
        return mapper.isKategoriExists(id) > 0;
    }

    public boolean isDropboxExists(int id) {
        return mapper.isDropboxExists(id) > 0;
    }



    public List<PermintaanPenjemputan> getAllPermintaan() {
        logger.debug("Fetching all permintaan");
        List<PermintaanPenjemputan> permintaanList = mapper.getAllPermintaan();
        logger.debug("Retrieved {} permintaan records", permintaanList.size());
        return permintaanList;
    }

    public PermintaanPenjemputan getPermintaanById(int id) {
        logger.debug("Fetching permintaan with ID: {}", id);
        PermintaanPenjemputan permintaan = mapper.getPermintaanById(id);
        if (permintaan == null) {
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
        } catch (Exception e) {
            logger.error("Failed to delete permintaan with ID: {}", id, e);
            sqlSession.rollback();
            throw e;
        }
    }

    public List<JenisKategori> getAllKategoriSampah() {
        logger.debug("Fetching all kategori sampah");
        List<JenisKategori> kategoriList = mapper.getAllKategoriSampah();
        logger.debug("Retrieved {} kategori records", kategoriList.size());
        return kategoriList;
    }

    public List<LokasiDropbox> getAllDropbox() {
        logger.debug("Fetching all dropbox locations");
        List<LokasiDropbox> dropboxList = dropboxService.getAllDropbox();
        logger.debug("Retrieved {} dropbox records", dropboxList.size());
        return dropboxList;
    }
}