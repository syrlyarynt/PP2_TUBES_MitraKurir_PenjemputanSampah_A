package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.JenisKategori;
import kelompok4.praktikumpemrograman2.model.JenisKategoriMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class JenisKategoriService {
    private final JenisKategoriMapper mapper;

    public JenisKategoriService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(JenisKategoriMapper.class);
    }

    public List<JenisKategori> getAllKategori() {
        return mapper.getAllKategori();
    }

    public JenisKategori getKategoriById(int id) {
        return mapper.getKategoriById(id);
    }

    public void createKategori(JenisKategori kategori) {
        mapper.insertKategori(kategori);
    }

    public void updateKategori(JenisKategori kategori) {
        mapper.updateKategori(kategori);
    }

    public void deleteKategori(int id) {
        mapper.deleteKategori(id);
    }
}
