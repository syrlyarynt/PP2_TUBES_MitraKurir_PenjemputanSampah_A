//package kelompok4.praktikumpemrograman2.services;
//
//import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
//import kelompok4.praktikumpemrograman2.model.PickupAssignment;
//import kelompok4.praktikumpemrograman2.model.PickupAssignmentMapper;
//import org.apache.ibatis.session.SqlSession;
//import java.util.List;
//
//public class PickupAssignmentService {
//    private final SqlSession sqlSession;
//    private final PickupAssignmentMapper mapper;
//
//    public PickupAssignmentService(SqlSession sqlSession) {
//        this.sqlSession = sqlSession;
//        this.mapper = sqlSession.getMapper(PickupAssignmentMapper.class);
//    }
//
//    public List<PickupAssignment> getAllAssignments() {
//        try (SqlSession session = MyBatisUtil.getSqlSession()) {
//            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
//            return localMapper.getAllAssignments();
//        }
//    }
//
//    public PickupAssignment getAssignmentById(int id) {
//        try (SqlSession session = MyBatisUtil.getSqlSession()) {
//            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
//            return localMapper.getAssignmentById(id);
//        }
//    }
//
////    public void createAssignment(PickupAssignment assignment) {
////        try (SqlSession session = MyBatisUtil.getSqlSession()) {
////            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
////            localMapper.insertAssignment(assignment);
////            session.commit();
////        } catch (Exception e) {
////            throw e;
////        }
////    }
//
//
//
//
//    public void createAssignment(PickupAssignment assignment, int kurirId) {
//        try (SqlSession session = MyBatisUtil.getSqlSession()) {
//            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
//            assignment.setKurirId(kurirId); // Menentukan kurir
//            localMapper.insertAssignment(assignment);
//            session.commit();
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//
//
//    public void updateAssignment(PickupAssignment assignment) {
//        try (SqlSession session = MyBatisUtil.getSqlSession()) {
//            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
//            localMapper.updateAssignment(assignment);
//            session.commit();
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//
//    public void deleteAssignment(int id) {
//        try (SqlSession session = MyBatisUtil.getSqlSession()) {
//            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
//            localMapper.deleteAssignment(id);
//            session.commit();
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//}


package kelompok4.praktikumpemrograman2.services;

import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import kelompok4.praktikumpemrograman2.model.PickupAssignment;
import kelompok4.praktikumpemrograman2.model.PickupAssignmentMapper;
import kelompok4.praktikumpemrograman2.utils.DataCache;
import org.apache.ibatis.session.SqlSession;
import java.util.List;
import java.util.Optional;

public class PickupAssignmentService {
    private static final String CACHE_KEY_ALL = "assignment_all";
    private static final String CACHE_KEY_PREFIX = "assignment_";
    private final SqlSession sqlSession;
    private final PickupAssignmentMapper mapper;

    public PickupAssignmentService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.mapper = sqlSession.getMapper(PickupAssignmentMapper.class);
    }

    public List<PickupAssignment> getAllAssignments() {
        Optional<Object> cached = DataCache.get(CACHE_KEY_ALL);
        if (cached.isPresent()) {
            return (List<PickupAssignment>) cached.get();
        }

        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
            List<PickupAssignment> assignments = localMapper.getAllAssignments();
            DataCache.put(CACHE_KEY_ALL, assignments);
            return assignments;
        }
    }

    public PickupAssignment getAssignmentById(int id) {
        String cacheKey = CACHE_KEY_PREFIX + id;
        Optional<Object> cached = DataCache.get(cacheKey);
        if (cached.isPresent()) {
            return (PickupAssignment) cached.get();
        }

        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
            PickupAssignment assignment = localMapper.getAssignmentById(id);
            if (assignment != null) {
                DataCache.put(cacheKey, assignment);
            }
            return assignment;
        }
    }

    public void createAssignment(PickupAssignment assignment, int kurirId) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
            assignment.setKurirId(kurirId);
            localMapper.insertAssignment(assignment);
            session.commit();
            invalidateCache();
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateAssignment(PickupAssignment assignment) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
            localMapper.updateAssignment(assignment);
            session.commit();
            invalidateCache();
            DataCache.put(CACHE_KEY_PREFIX + assignment.getId(), assignment);
        } catch (Exception e) {
            throw e;
        }
    }

    public void deleteAssignment(int id) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            PickupAssignmentMapper localMapper = session.getMapper(PickupAssignmentMapper.class);
            localMapper.deleteAssignment(id);
            session.commit();
            invalidateCache();
        } catch (Exception e) {
            throw e;
        }
    }

    private void invalidateCache() {
        DataCache.invalidateKeysByPrefix(CACHE_KEY_PREFIX);
        DataCache.remove(CACHE_KEY_ALL);
    }
}