package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.History;
import kelompok4.praktikumpemrograman2.model.HistoryMapper;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class HistoryService {
    public List<History> getAllHistory() {
        System.out.println("=== HistoryService.getAllHistory() START ===");
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.getSqlSession();
            System.out.println("SQL Session created successfully");

            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            System.out.println("Mapper created successfully");

            // Print SQL query being executed
            System.out.println("Executing SQL: SELECT * FROM permintaanpenjemputan");
            List<History> histories = mapper.getAllHistory();

            System.out.println("Query executed successfully");
            if (histories != null) {
                System.out.println("Retrieved " + histories.size() + " records");
                // Print first record details if exists
                if (!histories.isEmpty()) {
                    History first = histories.get(0);
                    System.out.println("First record - ID: " + first.getIdRiwayat()
                            + ", Status: " + first.getStatusPenyelesaian());
                }
            } else {
                System.out.println("Retrieved null list");
            }

            return histories;

        } catch (Exception e) {
            System.out.println("ERROR in getAllHistory: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
                System.out.println("SQL Session closed");
            }
            System.out.println("=== HistoryService.getAllHistory() END ===");
        }
    }


    public History getHistoryById(int id) {
        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            return mapper.getHistoryById(id);
        }
    }

    public void insertHistory(History history) {
        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            mapper.insertHistory(history);
            sqlSession.commit();
        }
    }

    public void updateHistory(History history) {
        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            mapper.updateHistory(history);
            sqlSession.commit();
        }
    }

    public void deleteHistory(int id) {
        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            HistoryMapper mapper = sqlSession.getMapper(HistoryMapper.class);
            mapper.deleteHistory(id);
            sqlSession.commit();
        }
    }
}