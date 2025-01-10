package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import kelompok4.praktikumpemrograman2.model.PickupAssignment;
import kelompok4.praktikumpemrograman2.model.PickupAssignmentMapper;
import org.apache.ibatis.session.SqlSession;
import java.util.List;

public class PickupAssignmentService {
    private final SqlSession sqlSession;
    private final PickupAssignmentMapper mapper;

    public PickupAssignmentService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.mapper = sqlSession.getMapper(PickupAssignmentMapper.class);
    }

    public List<PickupAssignment> getAllAssignments() {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
            return localMapper.getAllAssignments();
        }
    }

    public PickupAssignment getAssignmentById(int id) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
            return localMapper.getAssignmentById(id);
        }
    }

//    public void createAssignment(PickupAssignment assignment) {
//        try (SqlSession session = MyBatisUtil.getSqlSession()) {
//            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
//            localMapper.insertAssignment(assignment);
//            session.commit();
//        } catch (Exception e) {
//            throw e;
//        }
//    }




    public void createAssignment(PickupAssignment assignment, int kurirId) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
            assignment.setKurirId(kurirId); // Menentukan kurir
            localMapper.insertAssignment(assignment);
            session.commit();
        } catch (Exception e) {
            throw e;
        }
    }


    public void updateAssignment(PickupAssignment assignment) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
            localMapper.updateAssignment(assignment);
            session.commit();
        } catch (Exception e) {
            throw e;
        }
    }

    public void deleteAssignment(int id) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
            localMapper.deleteAssignment(id);
            session.commit();
        } catch (Exception e) {
            throw e;
        }
    }
}