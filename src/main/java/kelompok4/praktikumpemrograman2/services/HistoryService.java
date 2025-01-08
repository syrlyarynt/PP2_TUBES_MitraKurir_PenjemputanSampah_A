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

    public List<History> getAllHistory() {
        System.out.println("=== HistoryService.getAllHistory() START ===");
        try {
            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            System.out.println("Mapper created successfully");

            System.out.println("Executing SQL: SELECT * FROM history");
            List<History> histories = mapper.getAllHistory();

            System.out.println("Query executed successfully");
            if (histories != null) {
                System.out.println("Retrieved " + histories.size() + " records");
                if (!histories.isEmpty()) {
                    History first = histories.get(0);
                    System.out.println("First record - ID: " + first.getIdRiwayat()
                            + ", Status: " + first.getStatusPenyelesaian());
                }
            } else {
                System.out.println("Retrieved null list");
                histories = new ArrayList<>();
            }

            return histories;

        } catch (Exception e) {
            System.err.println("ERROR in getAllHistory: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            System.out.println("=== HistoryService.getAllHistory() END ===");
        }
    }

    public History getHistoryById(int id) {
        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
        return mapper.getHistoryById(id);
    }

    public void insertHistory(History history) {
        HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
        mapper.insertHistory(history);
        sqlSession.commit();
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