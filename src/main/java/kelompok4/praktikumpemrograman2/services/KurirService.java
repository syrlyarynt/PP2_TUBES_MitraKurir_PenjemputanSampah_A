package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.Kurir;
import kelompok4.praktikumpemrograman2.model.KurirMapper;
import org.apache.ibatis.session.SqlSession;
import java.util.List;

public class KurirService {
    private final SqlSession sqlSession;
    private final KurirMapper mapper;

    public KurirService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.mapper = sqlSession.getMapper(KurirMapper.class);
    }

    public List<Kurir> getAllKurir() {
        return mapper.getAllKurir();
    }

    public Kurir getKurirById(int id) {
        Kurir kurir = mapper.getKurirById(id);
        System.out.println("Service: Kurir = " + kurir); // Debugging
        return kurir;
    }

    public void insertKurir(Kurir kurir) {
        mapper.insertKurir(kurir);
        sqlSession.commit();
    }

    public void updateKurir(Kurir kurir) {
        mapper.updateKurir(kurir);
        sqlSession.commit();
    }

    public void deleteKurir(int id) {
        mapper.deleteKurir(id);
        sqlSession.commit();
    }
}
