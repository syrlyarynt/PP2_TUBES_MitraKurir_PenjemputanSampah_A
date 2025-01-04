package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import kelompok4.praktikumpemrograman2.model.LokasiDropboxMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class LokasiDropboxService {
    private final LokasiDropboxMapper mapper;
    private final SqlSession sqlSession;

    public LokasiDropboxService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(LokasiDropboxMapper.class);
        this.sqlSession = sqlSession;
    }

    public List<LokasiDropbox> getAllDropbox() {
        return mapper.getAllDropbox();
    }

    public LokasiDropbox getDropboxById(int id) {
        return mapper.getDropboxById(id);
    }

    public void createDropbox(LokasiDropbox dropbox) {
        mapper.insertDropbox(dropbox);
    }

    public void updateDropbox(LokasiDropbox dropbox) {
        try {
            mapper.updateDropbox(dropbox);
            sqlSession.commit(); // Add transaction commit
        } catch (Exception e) {
            sqlSession.rollback(); // Add transaction rollback
            throw e;
        }
    }

    public void deleteDropbox(int id) {
        try {
            mapper.deleteDropbox(id);
            sqlSession.commit(); // Add transaction commit
        } catch (Exception e) {
            sqlSession.rollback(); // Add transaction rollback
            throw e;
        }
    }





}
