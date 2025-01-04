package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputan;
import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputanMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class PermintaanPenjemputanService {
    private final PermintaanPenjemputanMapper mapper;

    public PermintaanPenjemputanService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(PermintaanPenjemputanMapper.class);
    }

    public List<PermintaanPenjemputan> getAllPermintaan() {
        return mapper.getAllPermintaan();
    }

    public PermintaanPenjemputan getPermintaanById(int id) {
        return mapper.getPermintaanById(id);
    }

    public void createPermintaan(PermintaanPenjemputan permintaan) {
        mapper.insertPermintaan(permintaan);
    }

    public void updatePermintaan(PermintaanPenjemputan permintaan) {
        mapper.updatePermintaan(permintaan);
    }

    public void deletePermintaan(int id) {
        mapper.deletePermintaan(id);
    }

}


