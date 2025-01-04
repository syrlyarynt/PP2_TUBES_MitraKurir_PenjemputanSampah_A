package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.DropboxRate;
import kelompok4.praktikumpemrograman2.services.DropboxRateService;
import org.apache.ibatis.session.SqlSession;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import java.util.List;

public class DropboxRateController {
    private final DropboxRateService service;

    public DropboxRateController() {
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        this.service = new DropboxRateService(sqlSession);
    }

    public List<DropboxRate> getAllRates() {
        return service.getAllRates();
    }

    public DropboxRate getRateById(int id) {
        return service.getRateById(id);
    }

    public void createRate(DropboxRate rate) {
        service.createRate(rate);
    }

    public void updateRate(DropboxRate rate) {
        service.updateRate(rate);
    }

    public void deleteRate(int id) {
        service.deleteRate(id);
    }
}