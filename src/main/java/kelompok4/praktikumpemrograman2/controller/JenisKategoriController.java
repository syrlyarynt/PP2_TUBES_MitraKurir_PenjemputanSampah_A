package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.JenisKategori;

import kelompok4.praktikumpemrograman2.services.JenisKategoriService;
import org.apache.ibatis.session.SqlSession;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;

import java.util.List;

public class JenisKategoriController {
    private final JenisKategoriService service;

    public JenisKategoriController() {
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        this.service = new JenisKategoriService(sqlSession);
    }

    public List<JenisKategori> getAllKategori() {
        return service.getAllKategori();
    }

    public JenisKategori getKategoriById(int id) {
        return service.getKategoriById(id);
    }

    public void createKategori(JenisKategori kategori) {
        service.createKategori(kategori);
    }

    public void updateKategori(JenisKategori kategori) {
        service.updateKategori(kategori);
    }

    public void deleteKategori(int id) {
        service.deleteKategori(id);
    }
}
