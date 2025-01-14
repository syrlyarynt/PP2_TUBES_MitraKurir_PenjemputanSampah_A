package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.History;
import kelompok4.praktikumpemrograman2.services.HistoryService;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class HistoryController {
    private final HistoryService historyService;
    private final SqlSession sqlSession;

    public HistoryController() {
        this.sqlSession = MyBatisUtil.getSqlSession();
        this.historyService = new HistoryService(sqlSession);
        System.out.println("HistoryController initialized");
    }

    public List<History> getAllHistory() {
        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            return historyService.getAllHistory(sqlSession);
        } catch (Exception e) {
            System.err.println("Error in getAllHistory: " + e.getMessage());
            throw e;
        }
    }

    public History getHistoryById(int id) {
        try {
            return historyService.getHistoryById(id);
        } catch (Exception e) {
            System.err.println("Error in getHistoryById: " + e.getMessage());
            throw e;
        }
    }

    public void insertHistory(History history) {
        try {
            historyService.insertHistory(history);
        } catch (Exception e) {
            System.err.println("Error in insertHistory: " + e.getMessage());
            throw e;
        }
    }

    public void updateHistory(History history) {
        try {
            historyService.updateHistory(history);
        } catch (Exception e) {
            System.err.println("Error in updateHistory: " + e.getMessage());
            throw e;
        }
    }

    public void deleteHistory(int id) {
        try {
            historyService.deleteHistory(id);
        } catch (Exception e) {
            System.err.println("Error in deleteHistory: " + e.getMessage());
            throw e;
        }
    }

    public void cleanup() {
        if (sqlSession != null) {
            sqlSession.close();
            System.out.println("HistoryController cleanup completed");
        }
    }
}