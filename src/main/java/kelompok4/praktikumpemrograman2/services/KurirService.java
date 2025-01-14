//package kelompok4.praktikumpemrograman2.services;
//
//import kelompok4.praktikumpemrograman2.model.Kurir;
//import kelompok4.praktikumpemrograman2.model.KurirMapper;
//import org.apache.ibatis.session.SqlSession;
//import java.util.List;
//
//public class KurirService {
//    private final SqlSession sqlSession;
//    private final KurirMapper mapper;
//
//    public KurirService(SqlSession sqlSession) {
//        this.sqlSession = sqlSession;
//        this.mapper = sqlSession.getMapper(KurirMapper.class);
//    }
//
//    public List<Kurir> getAllKurir() {
//        return mapper.getAllKurir();
//    }
//
//    public Kurir getKurirById(int id) {
//        Kurir kurir = mapper.getKurirById(id);
//        System.out.println("Service: Kurir = " + kurir); // Debugging
//        return kurir;
//    }
//
//    public void insertKurir(Kurir kurir) {
//        mapper.insertKurir(kurir);
//        sqlSession.commit();
//    }
//
//    public void updateKurir(Kurir kurir) {
//        mapper.updateKurir(kurir);
//        sqlSession.commit();
//    }
//
//    public void deleteKurir(int id) {
//        mapper.deleteKurir(id);
//        sqlSession.commit();
//    }
//}

package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.Kurir;
import kelompok4.praktikumpemrograman2.model.KurirMapper;
import kelompok4.praktikumpemrograman2.utils.DataCache;
import org.apache.ibatis.session.SqlSession;
import java.util.List;
import java.util.Optional;

public class KurirService {
    private static final String CACHE_KEY_ALL = "kurir_all";
    private static final String CACHE_KEY_PREFIX = "kurir_";
    private final SqlSession sqlSession;
    private final KurirMapper mapper;

    public KurirService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.mapper = sqlSession.getMapper(KurirMapper.class);
    }

    public List<Kurir> getAllKurir() {
        Optional<Object> cached = DataCache.get(CACHE_KEY_ALL);
        if (cached.isPresent()) {
            return (List<Kurir>) cached.get();
        }

        List<Kurir> kurirList = mapper.getAllKurir();
        DataCache.put(CACHE_KEY_ALL, kurirList);
        return kurirList;
    }

    public Kurir getKurirById(int id) {
        String cacheKey = CACHE_KEY_PREFIX + id;
        Optional<Object> cached = DataCache.get(cacheKey);
        if (cached.isPresent()) {
            return (Kurir) cached.get();
        }

        Kurir kurir = mapper.getKurirById(id);
        System.out.println("Service: Kurir = " + kurir);
        if (kurir != null) {
            DataCache.put(cacheKey, kurir);
        }
        return kurir;
    }

    public void insertKurir(Kurir kurir) {
        try {
            mapper.insertKurir(kurir);
            sqlSession.commit();
            invalidateCache();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public void updateKurir(Kurir kurir) {
        try {
            mapper.updateKurir(kurir);
            sqlSession.commit();
            invalidateCache();
            DataCache.put(CACHE_KEY_PREFIX + kurir.getId(), kurir);
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public void deleteKurir(int id) {
        try {
            mapper.deleteKurir(id);
            sqlSession.commit();
            invalidateCache();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    private void invalidateCache() {
        DataCache.invalidateKeysByPrefix(CACHE_KEY_PREFIX);
        DataCache.remove(CACHE_KEY_ALL);
    }
}