package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.History;
import kelompok4.praktikumpemrograman2.model.HistoryMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class HistoryService {
    private final SqlSession sqlSession;

    public HistoryService(SqlSession sqlSession) {
        if (sqlSession == null) {
            throw new IllegalArgumentException("SqlSession cannot be null");
        }
        this.sqlSession = sqlSession;
        System.out.println("HistoryService initialized with SqlSession");
    }

    public List<History> getAllHistory(SqlSession sqlSession) {
        System.out.println("=== HistoryService.getAllHistory() START ===");
        try {
            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            System.out.println("Mapper created successfully");

            List<History> histories = mapper.getAllHistory();
            System.out.println("Retrieved " + (histories != null ? histories.size() : 0) + " records");

            return histories != null ? histories : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("ERROR in getAllHistory: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void insertHistory(History history) {
        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
        mapper.insertHistory(history);
        sqlSession.commit();
    }

    public History getHistoryById(int id) {
        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
        return mapper.getHistoryById(id);
    }



    public void updateHistory(History history) {
        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
        mapper.updateHistory(history);
        sqlSession.commit();
    }

    public void deleteHistory(int id) {
        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
        mapper.deleteHistory(id);
        sqlSession.commit();
    }
}