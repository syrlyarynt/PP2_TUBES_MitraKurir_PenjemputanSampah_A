package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.PickupAssignment;
import kelompok4.praktikumpemrograman2.services.PickupAssignmentService;
import kelompok4.praktikumpemrograman2.model.History;
import kelompok4.praktikumpemrograman2.services.HistoryService;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.time.LocalDateTime;
import java.util.List;

public class PickupAssignmentController {
    private final PickupAssignmentService service;
    private final HistoryService historyService;
    private final SqlSession sqlSession;

    public PickupAssignmentController() {
        this.sqlSession = MyBatisUtil.getSqlSession();
        this.service = new PickupAssignmentService(sqlSession);
        this.historyService = new HistoryService(sqlSession);
        System.out.println("PickupAssignmentController initialized");
    }

    public List<PickupAssignment> getAllAssignments() {
        try {
            return service.getAllAssignments();
        } catch (Exception e) {
            System.err.println("Error in getAllAssignments: " + e.getMessage());
            throw e;
        }
    }

    public PickupAssignment getAssignmentById(int id) {
        try {
            return service.getAssignmentById(id);
        } catch (Exception e) {
            System.err.println("Error in getAssignmentById: " + e.getMessage());
            throw e;
        }
    }

    public void createAssignment(PickupAssignment assignment) {
        try {
            if (assignment.getStatus() == null || assignment.getStatus().isEmpty()) {
                assignment.setStatus("Assigned");
            }
            service.createAssignment(assignment);
        } catch (Exception e) {
            System.err.println("Error in createAssignment: " + e.getMessage());
            throw e;
        }
    }

    public void updateAssignment(PickupAssignment assignment) {
        try {
            PickupAssignment existing = service.getAssignmentById(assignment.getId());
            if (existing != null) {
                validateStatusTransition(existing.getStatus(), assignment.getStatus());
            }
            service.updateAssignment(assignment);
        } catch (Exception e) {
            System.err.println("Error in updateAssignment: " + e.getMessage());
            throw e;
        }
    }

    public void deleteAssignment(int id) {
        try {
            service.deleteAssignment(id);
        } catch (Exception e) {
            System.err.println("Error in deleteAssignment: " + e.getMessage());
            throw e;
        }
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        boolean isValid = switch (currentStatus) {
            case "Assigned" -> newStatus.equals("In Progress") || newStatus.equals("Cancelled") || newStatus.startsWith("Rejected");
            case "In Progress" -> newStatus.equals("Completed") || newStatus.equals("Cancelled");
            case "Completed", "Cancelled" -> false; // Final states
            default -> false;
        };

        if (!isValid) {
            throw new IllegalStateException("Invalid status transition from " +
                    currentStatus + " to " + newStatus);
        }
    }

    public boolean completeAssignment(int id, double actualWeight, double finalPrice, String notes) {
        try {
            PickupAssignment assignment = service.getAssignmentById(id);
            if (assignment == null) {
                throw new IllegalArgumentException("Assignment not found with id: " + id);
            }

            assignment.setStatus("Completed");
            assignment.setBeratAktual(new java.math.BigDecimal(actualWeight));
            assignment.setHargaFinal(new java.math.BigDecimal(finalPrice));
            assignment.setCompletionDate(LocalDateTime.now());
            assignment.setNotes(notes);

            service.updateAssignment(assignment);
            return true;
        } catch (Exception e) {
            System.err.println("Error completing assignment: " + e.getMessage());
            return false;
        }
    }

    public boolean rejectAssignment(int id, String reason) {
        try {
            PickupAssignment assignment = service.getAssignmentById(id);
            if (assignment == null) {
                throw new IllegalArgumentException("Assignment not found with id: " + id);
            }

            // Update the assignment's status to "Rejected"
            assignment.setStatus("Rejected - " + reason);
            assignment.setNotes(reason);
            service.updateAssignment(assignment);

            // Record the rejection in the history table
            History history = new History();
            history.setPickupAssignmentId(id);
            history.setWaktuSelesai(LocalDateTime.now());
            history.setLokasi(assignment.getDropbox().getNamaDropbox());
            history.setKategoriSampah(assignment.getPermintaan().getKategoriSampahId());
            history.setBeratSampah(assignment.getTotalWeight());
            history.setHarga(assignment.getTotalCost());
            history.setStatusPenyelesaian("Ditolak - " + reason);

            historyService.insertHistory(history);

            return true;
        } catch (Exception e) {
            System.err.println("Error rejecting assignment: " + e.getMessage());
            return false;
        }
    }

    public List<PickupAssignment> getAssignmentsByStatus(String status) {
        try {
            return getAllAssignments().stream()
                    .filter(a -> a.getStatus().equals(status))
                    .toList();
        } catch (Exception e) {
            System.err.println("Error getting assignments by status: " + e.getMessage());
            throw e;
        }
    }

    public void cleanup() {
        if (sqlSession != null) {
            sqlSession.close();
            System.out.println("PickupAssignmentController cleanup completed");
        }
    }

    public boolean updateAssignmentStatus(int id, String newStatus) {
        try {
            PickupAssignment assignment = service.getAssignmentById(id);
            if (assignment == null) {
                throw new IllegalArgumentException("Assignment not found with id: " + id);
            }

            validateStatusTransition(assignment.getStatus(), newStatus);
            assignment.setStatus(newStatus);
            service.updateAssignment(assignment);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating assignment status: " + e.getMessage());
            return false;
        }
    }
}
