//package kelompok4.praktikumpemrograman2.services;
//
//import kelompok4.praktikumpemrograman2.model.DropboxRate;
//import kelompok4.praktikumpemrograman2.model.DropboxRateMapper;
//import org.apache.ibatis.session.SqlSession;
//import java.util.List;
//
//public class DropboxRateService {
//    private final DropboxRateMapper mapper;
//    private final SqlSession sqlSession;
//
//    public DropboxRateService(SqlSession sqlSession) {
//        this.mapper = sqlSession.getMapper(DropboxRateMapper.class);
//        this.sqlSession = sqlSession;
//    }
//
//    public List<DropboxRate> getAllRates() {
//        return mapper.getAllRates();
//    }
//
//    public DropboxRate getRateById(int id) {
//        return mapper.getRateById(id);
//    }
//
//    public void createRate(DropboxRate rate) {
//        try {
//            mapper.insertRate(rate);
//            sqlSession.commit();
//        } catch (Exception e) {
//            sqlSession.rollback();
//            throw e;
//        }
//    }
//
//    public void updateRate(DropboxRate rate) {
//        try {
//            mapper.updateRate(rate);
//            sqlSession.commit();
//        } catch (Exception e) {
//            sqlSession.rollback();
//            throw e;
//        }
//    }
//
//    public void deleteRate(int id) {
//        try {
//            mapper.deleteRate(id);
//            sqlSession.commit();
//        } catch (Exception e) {
//            sqlSession.rollback();
//            throw e;
//        }
//    }
//}

package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.DropboxRate;
import kelompok4.praktikumpemrograman2.model.DropboxRateMapper;
import kelompok4.praktikumpemrograman2.utils.DataCache;
import org.apache.ibatis.session.SqlSession;
import java.util.List;
import java.util.Optional;

public class DropboxRateService {
    private static final String CACHE_KEY_ALL = "rates_all";
    private static final String CACHE_KEY_PREFIX = "rate_";
    private final DropboxRateMapper mapper;
    private final SqlSession sqlSession;

    public DropboxRateService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(DropboxRateMapper.class);
        this.sqlSession = sqlSession;
    }

    public List<DropboxRate> getAllRates() {
        Optional<Object> cached = DataCache.get(CACHE_KEY_ALL);
        if (cached.isPresent()) {
            return (List<DropboxRate>) cached.get();
        }
        List<DropboxRate> rates = mapper.getAllRates();
        DataCache.put(CACHE_KEY_ALL, rates);
        return rates;
    }

    public DropboxRate getRateById(int id) {
        String cacheKey = CACHE_KEY_PREFIX + id;
        Optional<Object> cached = DataCache.get(cacheKey);
        if (cached.isPresent()) {
            return (DropboxRate) cached.get();
        }
        DropboxRate rate = mapper.getRateById(id);
        if (rate != null) {
            DataCache.put(cacheKey, rate);
        }
        return rate;
    }

    public void createRate(DropboxRate rate) {
        try {
            mapper.insertRate(rate);
            sqlSession.commit();
            invalidateCache();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public void updateRate(DropboxRate rate) {
        try {
            mapper.updateRate(rate);
            sqlSession.commit();
            invalidateCache();
            DataCache.put(CACHE_KEY_PREFIX + rate.getId(), rate);
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public void deleteRate(int id) {
        try {
            mapper.deleteRate(id);
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