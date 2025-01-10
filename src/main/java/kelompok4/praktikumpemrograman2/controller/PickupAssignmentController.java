

package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.Kurir;
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
    private final KurirController kurirController;

    public PickupAssignmentController() {
        this.sqlSession = MyBatisUtil.getSqlSession();
        this.service = new PickupAssignmentService(sqlSession);
        this.historyService = new HistoryService(sqlSession);
        this.kurirController = new KurirController();
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

//    public void createAssignment(PickupAssignment assignment, int kurirId) {
//        try {
//            if (assignment.getStatus() == null || assignment.getStatus().isEmpty()) {
//                assignment.setStatus("Assigned");
//            }
//            assignment.setKurirId(kurirId); // Menambahkan kurir ke tugas
//            service.createAssignment(assignment, kurirId);
//        } catch (Exception e) {
//            System.err.println("Error in createAssignment: " + e.getMessage());
//            throw e;
//        }
//    }

    public void createAssignment(PickupAssignment assignment, int kurirId) {
        try {
            // Update courier status to Busy
            Kurir kurir = kurirController.getKurirById(kurirId);
            if (kurir != null) {
                kurirController.updateKurir(
                        kurirId,
                        kurir.getNama(),
                        kurir.getNomorTelepon(),
                        kurir.getAlamatKurir(),
                        "Busy"
                );
            }

            if (assignment.getStatus() == null || assignment.getStatus().isEmpty()) {
                assignment.setStatus("Assigned");
            }
            assignment.setKurirId(kurirId);
            service.createAssignment(assignment, kurirId);
        } catch (Exception e) {
            System.err.println("Error in createAssignment: " + e.getMessage());
            throw e;
        }
    }

    // New method to update courier status
    public void updateCourierStatus(int kurirId, String status) {
        try {
            Kurir kurir = kurirController.getKurirById(kurirId);
            if (kurir != null) {
                kurirController.updateKurir(
                        kurirId,
                        kurir.getNama(),
                        kurir.getNomorTelepon(),
                        kurir.getAlamatKurir(),
                        status
                );
            }
        } catch (Exception e) {
            System.err.println("Error updating courier status: " + e.getMessage());
            throw e;
        }
    }

    public void updateAssignment(PickupAssignment assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment cannot be null");
        }

        try {
            System.out.println("Fetching existing assignment with ID: " + assignment.getId());
            PickupAssignment existing = service.getAssignmentById(assignment.getId());

            if (existing != null) {
                System.out.println("Validating status transition...");
                validateStatusTransition(existing.getStatus(), assignment.getStatus());
            } else {
                throw new IllegalArgumentException("Assignment with ID " + assignment.getId() + " not found.");
            }

            System.out.println("Updating assignment in the database...");
            assignment.setKurirId(existing.getKurirId()); // Memastikan kurir tetap sama jika tidak diubah
            service.updateAssignment(assignment);

            System.out.println("Assignment updated successfully: " + assignment);
        } catch (Exception e) {
            System.err.println("Error in updateAssignment: " + e.getMessage());
            throw new RuntimeException("Failed to update assignment: " + e.getMessage(), e);
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

//    public boolean completeAssignment(int id, double actualWeight, double finalPrice, String notes) {
//        try {
//            PickupAssignment assignment = service.getAssignmentById(id);
//            if (assignment == null) {
//                throw new IllegalArgumentException("Assignment not found with id: " + id);
//            }
//
//            // Pastikan tugas sudah memiliki kurir sebelum diselesaikan
//            if (assignment.getKurirId() == 0) {
//                throw new IllegalStateException("Cannot complete assignment without a designated courier.");
//            }
//
//            assignment.setStatus("Completed");
//            assignment.setBeratAktual(new java.math.BigDecimal(actualWeight));
//            assignment.setHargaFinal(new java.math.BigDecimal(finalPrice));
//            assignment.setCompletionDate(LocalDateTime.now());
//            assignment.setNotes(notes);
//
//            service.updateAssignment(assignment);
//            return true;
//        } catch (Exception e) {
//            System.err.println("Error completing assignment: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean rejectAssignment(int id, String reason) {
//        try {
//            PickupAssignment assignment = service.getAssignmentById(id);
//            if (assignment == null) {
//                throw new IllegalArgumentException("Assignment not found with id: " + id);
//            }
//
//            assignment.setStatus("Rejected - " + reason);
//            assignment.setNotes(reason);
//            service.updateAssignment(assignment);
//
//            History history = new History();
//            history.setPickupAssignmentId(id);
//            history.setWaktuSelesai(LocalDateTime.now());
//            history.setLokasi(assignment.getDropbox().getNamaDropbox());
//            history.setKategoriSampah(assignment.getPermintaan().getKategoriSampahId());
//            history.setBeratSampah(assignment.getTotalWeight());
//            history.setHarga(assignment.getTotalCost());
//            history.setStatusPenyelesaian("Ditolak - " + reason);
//
//            historyService.insertHistory(history);
//
//            return true;
//        } catch (Exception e) {
//            System.err.println("Error rejecting assignment: " + e.getMessage());
//            return false;
//        }
//    }

    public boolean completeAssignment(int id, double actualWeight, double finalPrice, String notes) {
        try {
            PickupAssignment assignment = service.getAssignmentById(id);
            if (assignment == null) {
                throw new IllegalArgumentException("Assignment not found with id: " + id);
            }

            // Pastikan tugas sudah memiliki kurir sebelum diselesaikan
            if (assignment.getKurirId() == 0) {
                throw new IllegalStateException("Cannot complete assignment without a designated courier.");
            }

            assignment.setStatus("Completed");
            assignment.setBeratAktual(new java.math.BigDecimal(actualWeight));
            assignment.setHargaFinal(new java.math.BigDecimal(finalPrice));
            assignment.setCompletionDate(LocalDateTime.now());
            assignment.setNotes(notes);

            service.updateAssignment(assignment);

            // Update courier status back to Available
            updateCourierStatus(assignment.getKurirId(), "Available");

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

            assignment.setStatus("Rejected - " + reason);
            assignment.setNotes(reason);
            service.updateAssignment(assignment);

            // Update courier status back to Available
            updateCourierStatus(assignment.getKurirId(), "Available");

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
