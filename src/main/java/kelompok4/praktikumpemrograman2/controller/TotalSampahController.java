package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.TotalSampah;
import kelompok4.praktikumpemrograman2.services.TotalSampahService;
import org.apache.ibatis.session.SqlSession;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;

import java.util.List;

public class TotalSampahController {
    private final TotalSampahService service;

    public TotalSampahController() {
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        this.service = new TotalSampahService(sqlSession);
    }

    public List<TotalSampah> getAll() {
        return service.getAll();
    }



    // Method for filtering by date
    public List<TotalSampah> getTotalSampahByTanggal(String date) {
        return service.filterByDay(date);
    }

    // Method for filtering by week
    public List<TotalSampah> getTotalSampahByWeek(String startDate, String endDate) {
        return service.filterByWeek(startDate, endDate);
    }

    // Method for filtering by month
    public List<TotalSampah> getTotalSampahByMonth(String startDate, String endDate) {
        return service.filterByMonth(startDate, endDate);
    }
}
