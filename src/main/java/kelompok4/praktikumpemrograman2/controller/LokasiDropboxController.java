package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import kelompok4.praktikumpemrograman2.services.LokasiDropboxService;
import org.apache.ibatis.session.SqlSession;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;

import java.util.List;

public class LokasiDropboxController {
    private final LokasiDropboxService service;

    public LokasiDropboxController() {
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        this.service = new LokasiDropboxService(sqlSession);
    }

    public List<LokasiDropbox> getAllDropbox() {
        return service.getAllDropbox();
    }

    public LokasiDropbox getDropboxById(int id) {
        return service.getDropboxById(id);
    }

    public void createDropbox(LokasiDropbox dropbox) {
        service.createDropbox(dropbox);
    }

    public void updateDropbox(LokasiDropbox dropbox) {
        service.updateDropbox(dropbox);
    }

    public void deleteDropbox(int id) {
        service.deleteDropbox(id);
    }


}
