package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.History;
import kelompok4.praktikumpemrograman2.services.HistoryService;

import java.util.List;

public class HistoryController {
    private final HistoryService historyService;

    public HistoryController() {
        this.historyService = new HistoryService();
    }

    public List<History> getAllHistory() {
        return historyService.getAllHistory();
    }

    public History getHistoryById(int id) {
        return historyService.getHistoryById(id);
    }

    public void insertHistory(History history) {
        historyService.insertHistory(history);
    }

    public void updateHistory(History history) {
        historyService.updateHistory(history);
    }

    public void deleteHistory(int id) {
        historyService.deleteHistory(id);
    }
}