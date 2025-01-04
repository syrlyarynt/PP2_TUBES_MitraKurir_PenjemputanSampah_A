package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import kelompok4.praktikumpemrograman2.model.LokasiDropboxMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class LokasiDropboxService {
    private final LokasiDropboxMapper mapper;
    private final SqlSession sqlSession;

    public LokasiDropboxService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(LokasiDropboxMapper.class);
        this.sqlSession = sqlSession;
    }

    public List<LokasiDropbox> getAllDropbox() {
        System.out.println("=== LokasiDropboxService.getAllDropbox() START ===");
        try {
            System.out.println("Executing SQL: SELECT * FROM lokasi_dropbox");
            List<LokasiDropbox> dropboxes = mapper.getAllDropbox();
            System.out.println("Query executed successfully");

            if (dropboxes != null) {
                System.out.println("Retrieved " + dropboxes.size() + " records");
                if (!dropboxes.isEmpty()) {
                    LokasiDropbox first = dropboxes.get(0);
                    System.out.println("First record - ID: " + first.getId() + ", Nama Dropbox: " + first.getNamaDropbox());
                }
            } else {
                System.out.println("Retrieved null list");
            }

            return dropboxes;
        } catch (Exception e) {
            System.out.println("ERROR in getAllDropbox: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            System.out.println("=== LokasiDropboxService.getAllDropbox() END ===");
        }
    }

    public LokasiDropbox getDropboxById(int id) {
        System.out.println("=== LokasiDropboxService.getDropboxById() START ===");
        try {
            System.out.println("Executing SQL: SELECT * FROM lokasi_dropbox WHERE id = " + id);
            LokasiDropbox dropbox = mapper.getDropboxById(id);
            if (dropbox != null) {
                System.out.println("Record found - ID: " + dropbox.getId() + ", Nama Dropbox: " + dropbox.getNamaDropbox());
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
        } catch (Exception e) {
            System.out.println("ERROR in createDropbox: " + e.getMessage());
            e.printStackTrace();
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
        } catch (Exception e) {
            System.out.println("ERROR in updateDropbox: " + e.getMessage());
            e.printStackTrace();
            sqlSession.rollback();
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
        } catch (Exception e) {
            System.out.println("ERROR in deleteDropbox: " + e.getMessage());
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            System.out.println("=== LokasiDropboxService.deleteDropbox() END ===");
        }
    }
}
