package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.TotalSampah;
import kelompok4.praktikumpemrograman2.model.TotalSampahMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class TotalSampahService {
    private final TotalSampahMapper mapper;
    private final SqlSession sqlSession;

    public TotalSampahService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(TotalSampahMapper.class);
        this.sqlSession = sqlSession;
    }

    public List<TotalSampah> getAll() {
        List<TotalSampah> result = mapper.getAll();
        System.out.println("Retrieved " + result.size() + " records");
        result.forEach(System.out::println);
        return result;
    }





    public List<TotalSampah> filterByDay(String date) {
        return mapper.getTotalSampahByTanggal(date);
    }

    // Filter berdasarkan minggu (menggunakan parameter startDate dan endDate)
    public List<TotalSampah> filterByWeek(String startDate, String endDate) {
        return mapper.getTotalSampahByWeek(startDate, endDate);
    }

    // Filter berdasarkan bulan (menggunakan parameter startDate dan endDate)
    public List<TotalSampah> filterByMonth(String startDate, String endDate) {
        return mapper.getTotalSampahByMonth(startDate, endDate);
    }
}
