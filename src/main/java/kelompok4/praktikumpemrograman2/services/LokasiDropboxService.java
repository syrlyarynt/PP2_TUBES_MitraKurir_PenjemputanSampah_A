//package kelompok4.praktikumpemrograman2.services;
//
//import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
//import kelompok4.praktikumpemrograman2.model.LokasiDropboxMapper;
//import org.apache.ibatis.session.SqlSession;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class LokasiDropboxService {
//    private final LokasiDropboxMapper mapper;
//    private final SqlSession sqlSession;
//
//    public LokasiDropboxService(SqlSession sqlSession) {
//        this.mapper = sqlSession.getMapper(LokasiDropboxMapper.class);
//        this.sqlSession = sqlSession;
//    }
//
//    public List<LokasiDropbox> getAllDropbox() {
//        System.out.println("=== LokasiDropboxController.getAllDropbox START ===");
//        sqlSession.clearCache();
//        List<LokasiDropbox> dropboxes = mapper.getAllDropbox();
//
//        if (dropboxes == null || dropboxes.isEmpty()) {
//            System.out.println("Tidak ada data dropbox ditemukan!");
//        } else {
//            System.out.println("Retrieved " + dropboxes.size() + " dropbox records.");
//            for (LokasiDropbox dropbox : dropboxes) {
//                System.out.println("Dropbox ID: " + dropbox.getId() + ", Name: " + dropbox.getNamaDropbox());
//            }
//        }
//
//        System.out.println("=== LokasiDropboxController.getAllDropbox END ===");
//        return dropboxes;
//    }
//
//
//    public LokasiDropbox getDropboxById(int id) {
//        System.out.println("=== LokasiDropboxService.getDropboxById() START ===");
//        try {
//            System.out.println("Executing SQL: SELECT * FROM lokasi_dropbox WHERE id = " + id);
//            LokasiDropbox dropbox = mapper.getDropboxById(id);
//            if (dropbox != null) {
//                System.out.println("Record found - ID: " + dropbox.getId() + ", Nama Dropbox: " + dropbox.getNamaDropbox());
//            } else {
//                System.out.println("No record found with ID: " + id);
//            }
//            return dropbox;
//        } catch (Exception e) {
//            System.out.println("ERROR in getDropboxById: " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        } finally {
//            System.out.println("=== LokasiDropboxService.getDropboxById() END ===");
//        }
//    }
//
//    public void createDropbox(LokasiDropbox dropbox) {
//        System.out.println("=== LokasiDropboxService.createDropbox() START ===");
//        try {
//            // Menampilkan log untuk memeriksa data sebelum disimpan
//            System.out.println("Inserting Dropbox: " + dropbox);
//
//            // Menyisipkan data dropbox ke database
//            mapper.insertDropbox(dropbox);
//
//            // Melakukan commit untuk memastikan data tersimpan
//            sqlSession.commit();
//
//            sqlSession.flushStatements();
//
//            // Membersihkan cache untuk memastikan data terbaru diambil
//            sqlSession.clearCache();
//
//            // Menampilkan ID setelah data berhasil dimasukkan
//            System.out.println("Dropbox inserted successfully with ID: " + dropbox.getId());
//
//            // Fetch dan tampilkan semua dropbox untuk memastikan data terbaru ada
//            List<LokasiDropbox> dropboxes = mapper.getAllDropbox();
//            dropboxes.forEach(d -> System.out.println("Dropbox ID: " + d.getId() + ", Name: " + d.getNamaDropbox()));
//
//        } catch (Exception e) {
//            System.out.println("ERROR in createDropbox: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            System.out.println("=== LokasiDropboxService.createDropbox() END ===");
//        }
//    }
//
//
//
//
//    public void updateDropbox(LokasiDropbox dropbox) {
//        System.out.println("=== LokasiDropboxService.updateDropbox() START ===");
//        try {
//            System.out.println("Updating Dropbox with ID: " + dropbox.getId());
//            mapper.updateDropbox(dropbox);
//            sqlSession.commit();
//            System.out.println("Dropbox updated successfully");
//        } catch (Exception e) {
//            System.out.println("ERROR in updateDropbox: " + e.getMessage());
//            e.printStackTrace();
//            sqlSession.rollback();
//        } finally {
//            System.out.println("=== LokasiDropboxService.updateDropbox() END ===");
//        }
//    }
//
//    public void deleteDropbox(int id) {
//        System.out.println("=== LokasiDropboxService.deleteDropbox() START ===");
//        try {
//            System.out.println("Deleting Dropbox with ID: " + id);
//            mapper.deleteDropbox(id);
//            sqlSession.commit();
//            System.out.println("Dropbox deleted successfully");
//        } catch (Exception e) {
//            System.out.println("ERROR in deleteDropbox: " + e.getMessage());
//            e.printStackTrace();
//            sqlSession.rollback();
//        } finally {
//            System.out.println("=== LokasiDropboxService.deleteDropbox() END ===");
//        }
//    }
//}

package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import kelompok4.praktikumpemrograman2.model.LokasiDropboxMapper;
import kelompok4.praktikumpemrograman2.utils.DataCache;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class LokasiDropboxService {
    private static final String CACHE_KEY_ALL = "dropbox_all";
    private static final String CACHE_KEY_PREFIX = "dropbox_";
    private final LokasiDropboxMapper mapper;
    private final SqlSession sqlSession;

    public LokasiDropboxService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(LokasiDropboxMapper.class);
        this.sqlSession = sqlSession;
    }

    public List<LokasiDropbox> getAllDropbox() {
        Optional<Object> cached = DataCache.get(CACHE_KEY_ALL);
        if (cached.isPresent()) {
            return (List<LokasiDropbox>) cached.get();
        }

        System.out.println("=== LokasiDropboxController.getAllDropbox START ===");
        List<LokasiDropbox> dropboxes = mapper.getAllDropbox();

        if (dropboxes != null && !dropboxes.isEmpty()) {
            System.out.println("Retrieved " + dropboxes.size() + " dropbox records.");
            for (LokasiDropbox dropbox : dropboxes) {
                System.out.println("Dropbox ID: " + dropbox.getId() + ", Name: " + dropbox.getNamaDropbox());
            }
            DataCache.put(CACHE_KEY_ALL, dropboxes);
        } else {
            System.out.println("Tidak ada data dropbox ditemukan!");
        }

        System.out.println("=== LokasiDropboxController.getAllDropbox END ===");
        return dropboxes;
    }

    public LokasiDropbox getDropboxById(int id) {
        String cacheKey = CACHE_KEY_PREFIX + id;
        Optional<Object> cached = DataCache.get(cacheKey);
        if (cached.isPresent()) {
            return (LokasiDropbox) cached.get();
        }

        System.out.println("=== LokasiDropboxService.getDropboxById() START ===");
        try {
            System.out.println("Executing SQL: SELECT * FROM lokasi_dropbox WHERE id = " + id);
            LokasiDropbox dropbox = mapper.getDropboxById(id);
            if (dropbox != null) {
                System.out.println("Record found - ID: " + dropbox.getId() + ", Nama Dropbox: " + dropbox.getNamaDropbox());
                DataCache.put(cacheKey, dropbox);
            } else {
                System.out.println("No record found with ID: " + id);
            }
            return dropbox;
        } catch (Exception e) {
            System.out.println("ERROR in getDropboxById: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            System.out.println("=== LokasiDropboxService.getDropboxById() END ===");
        }
    }

    public void createDropbox(LokasiDropbox dropbox) {
        System.out.println("=== LokasiDropboxService.createDropbox() START ===");
        try {
            System.out.println("Inserting Dropbox: " + dropbox);
            mapper.insertDropbox(dropbox);
            sqlSession.commit();
            System.out.println("Dropbox inserted successfully with ID: " + dropbox.getId());
            invalidateCache();
        } catch (Exception e) {
            sqlSession.rollback();
            System.out.println("ERROR in createDropbox: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            System.out.println("=== LokasiDropboxService.createDropbox() END ===");
        }
    }

    public void updateDropbox(LokasiDropbox dropbox) {
        System.out.println("=== LokasiDropboxService.updateDropbox() START ===");
        try {
            System.out.println("Updating Dropbox with ID: " + dropbox.getId());
            mapper.updateDropbox(dropbox);
            sqlSession.commit();
            System.out.println("Dropbox updated successfully");
            invalidateCache();
            DataCache.put(CACHE_KEY_PREFIX + dropbox.getId(), dropbox);
        } catch (Exception e) {
            sqlSession.rollback();
            System.out.println("ERROR in updateDropbox: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            System.out.println("=== LokasiDropboxService.updateDropbox() END ===");
        }
    }

    public void deleteDropbox(int id) {
        System.out.println("=== LokasiDropboxService.deleteDropbox() START ===");
        try {
            System.out.println("Deleting Dropbox with ID: " + id);
            mapper.deleteDropbox(id);
            sqlSession.commit();
            System.out.println("Dropbox deleted successfully");
            invalidateCache();
        } catch (Exception e) {
            sqlSession.rollback();
            System.out.println("ERROR in deleteDropbox: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            System.out.println("=== LokasiDropboxService.deleteDropbox() END ===");
        }
    }

    private void invalidateCache() {
        DataCache.invalidateKeysByPrefix(CACHE_KEY_PREFIX);
        DataCache.remove(CACHE_KEY_ALL);
    }
}