//package kelompok4.praktikumpemrograman2.services;
//
//import kelompok4.praktikumpemrograman2.model.History;
//import kelompok4.praktikumpemrograman2.model.HistoryMapper;
//import org.apache.ibatis.session.SqlSession;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HistoryService {
//    private final SqlSession sqlSession;
//
//    public HistoryService(SqlSession sqlSession) {
//        if (sqlSession == null) {
//            throw new IllegalArgumentException("SqlSession cannot be null");
//        }
//        this.sqlSession = sqlSession;
//        System.out.println("HistoryService initialized with SqlSession");
//    }
//
//    public List<History> getAllHistory(SqlSession sqlSession) {
//        System.out.println("=== HistoryService.getAllHistory() START ===");
//        try {
//            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
//            System.out.println("Mapper created successfully");
//
//            List<History> histories = mapper.getAllHistory();
//            System.out.println("Retrieved " + (histories != null ? histories.size() : 0) + " records");
//
//            return histories != null ? histories : new ArrayList<>();
//        } catch (Exception e) {
//            System.err.println("ERROR in getAllHistory: " + e.getMessage());
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }
//
//    public void insertHistory(History history) {
//        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
//        mapper.insertHistory(history);
//        sqlSession.commit();
//    }
//
//    public History getHistoryById(int id) {
//        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
//        return mapper.getHistoryById(id);
//    }
//
//
//
//    public void updateHistory(History history) {
//        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
//        mapper.updateHistory(history);
//        sqlSession.commit();
//    }
//
//    public void deleteHistory(int id) {
//        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
//        mapper.deleteHistory(id);
//        sqlSession.commit();
//    }
//}

package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.History;
import kelompok4.praktikumpemrograman2.model.HistoryMapper;
import kelompok4.praktikumpemrograman2.utils.DataCache;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoryService {
    private static final String CACHE_KEY_ALL = "history_all";
    private static final String CACHE_KEY_PREFIX = "history_";
    private final SqlSession sqlSession;

    public HistoryService(SqlSession sqlSession) {
        if (sqlSession == null) {
            throw new IllegalArgumentException("SqlSession cannot be null");
        }
        this.sqlSession = sqlSession;
        System.out.println("HistoryService initialized with SqlSession");
    }

    public List<History> getAllHistory(SqlSession sqlSession) {
        Optional<Object> cached = DataCache.get(CACHE_KEY_ALL);
        if (cached.isPresent()) {
            return (List<History>) cached.get();
        }

        System.out.println("=== HistoryService.getAllHistory() START ===");
        try {
            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            System.out.println("Mapper created successfully");

            List<History> histories = mapper.getAllHistory();
            System.out.println("Retrieved " + (histories != null ? histories.size() : 0) + " records");

            List<History> result = histories != null ? histories : new ArrayList<>();
            DataCache.put(CACHE_KEY_ALL, result);
            return result;
        } catch (Exception e) {
            System.err.println("ERROR in getAllHistory: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public History getHistoryById(int id) {
        String cacheKey = CACHE_KEY_PREFIX + id;
        Optional<Object> cached = DataCache.get(cacheKey);
        if (cached.isPresent()) {
            return (History) cached.get();
        }

        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
        History history = mapper.getHistoryById(id);
        if (history != null) {
            DataCache.put(cacheKey, history);
        }
        return history;
    }

    public void insertHistory(History history) {
        try {
            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            mapper.insertHistory(history);
            sqlSession.commit();
            invalidateCache();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public void updateHistory(History history) {
        try {
            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            mapper.updateHistory(history);
            sqlSession.commit();
            invalidateCache();
            DataCache.put(CACHE_KEY_PREFIX + history.getIdRiwayat(), history);
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public void deleteHistory(int id) {
        try {
            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            mapper.deleteHistory(id);
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