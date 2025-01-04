package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.DropboxRate;
import kelompok4.praktikumpemrograman2.model.DropboxRateMapper;
import org.apache.ibatis.session.SqlSession;
import java.util.List;

public class DropboxRateService {
    private final DropboxRateMapper mapper;
    private final SqlSession sqlSession;

    public DropboxRateService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(DropboxRateMapper.class);
        this.sqlSession = sqlSession;
    }

    public List<DropboxRate> getAllRates() {
        return mapper.getAllRates();
    }

    public DropboxRate getRateById(int id) {
        return mapper.getRateById(id);
    }

    public void createRate(DropboxRate rate) {
        try {
            mapper.insertRate(rate);
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public void updateRate(DropboxRate rate) {
        try {
            mapper.updateRate(rate);
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public void deleteRate(int id) {
        try {
            mapper.deleteRate(id);
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }
}