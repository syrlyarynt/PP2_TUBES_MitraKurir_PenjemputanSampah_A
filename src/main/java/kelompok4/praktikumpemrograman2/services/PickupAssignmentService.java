package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.PickupAssignment;
import kelompok4.praktikumpemrograman2.model.PickupAssignmentMapper;
import org.apache.ibatis.session.SqlSession;
import java.util.List;

public class PickupAssignmentService {
    private final PickupAssignmentMapper mapper;
    private final SqlSession sqlSession;

    public PickupAssignmentService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(PickupAssignmentMapper.class);
        this.sqlSession = sqlSession;
    }

    public List<PickupAssignment> getAllAssignments() {
        return mapper.getAllAssignments();
    }

    public PickupAssignment getAssignmentById(int id) {
        return mapper.getAssignmentById(id);
    }

    public void createAssignment(PickupAssignment assignment) {
        try {
            mapper.insertAssignment(assignment);
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public void updateAssignment(PickupAssignment assignment) {
        try {
            mapper.updateAssignment(assignment);
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public void deleteAssignment(int id) {
        try {
            mapper.deleteAssignment(id);
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }
}