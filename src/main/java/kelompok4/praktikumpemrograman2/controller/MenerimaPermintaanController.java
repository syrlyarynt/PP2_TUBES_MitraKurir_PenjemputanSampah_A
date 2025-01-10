    package kelompok4.praktikumpemrograman2.controller;

    import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputan;
    import kelompok4.praktikumpemrograman2.model.PickupAssignment;
    import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
    import org.apache.ibatis.session.SqlSession;
    import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
    import java.util.List;
    import java.util.stream.Stream;

    public class MenerimaPermintaanController {
        private final PickupAssignmentController pickupController;
        private final LokasiDropboxController dropboxController;
        private final PermintaanPenjemputanController permintaanController;
        private PermintaanPenjemputan currentPermintaan;
        private PickupAssignment currentAssignment;

        public MenerimaPermintaanController() {
            this.pickupController = new PickupAssignmentController();
            this.permintaanController = new PermintaanPenjemputanController();
            this.dropboxController = new LokasiDropboxController();
        }

        // Method untuk mendapatkan semua assignment
        public List<PickupAssignment> getAllAssignments() {
            return pickupController.getAllAssignments();
        }

        // Method untuk mendapatkan data pickup yang akan ditampilkan
//        public Object[][] getPickupTableData() {
//            List<PickupAssignment> assignments = pickupController.getAllAssignments();
//            return assignments.stream()
//                    .filter(assignment -> assignment != null && assignment.getPermintaan() != null)
//                    .map(assignment -> new Object[] {
//                            assignment.getPermintaan().getNamaPelanggan(),
//                            assignment.getPermintaan().getAlamat(),
//                            assignment.getTotalWeight().toString(),
//                            assignment.getTotalCost().toString(),
//                            assignment.getStatus() // Menambahkan status ke data tabel
//                    })
//                    .toArray(Object[][]::new);
//        }

        // Method untuk mendapatkan data pickup yang akan ditampilkan
        public Object[][] getPickupTableData() {
            List<PickupAssignment> assignments = pickupController.getAllAssignments();
            return assignments.stream()
                    .filter(assignment -> assignment != null && assignment.getPermintaan() != null)
                    .map(assignment -> new Object[] {
                            assignment.getPermintaan().getNamaPelanggan(),
                            assignment.getPermintaan().getAlamat(),
                            assignment.getTotalWeight().toString(),
                            assignment.getTotalCost().toString(),
                            assignment.getStatus(),  // Status
                            assignment.getKurir() != null ?
                                    assignment.getKurir().getNama() :
                                    "Belum Ditentukan"   // Nama Kurir
                    })
                    .toArray(Object[][]::new);
        }

        // Method untuk mendapatkan data dropbox
        public Object[][] getDropboxTableData() {
            List<LokasiDropbox> dropboxList = dropboxController.getAllDropbox();
            return dropboxList.stream()
                    .map(dropbox -> new Object[] {
                            dropbox.getNamaDropbox(),
                            dropbox.getAlamat(),
                            dropbox.getJarak() + " KM",
                            dropbox.getKapasitasMax().subtract(dropbox.getKapasitasTerisi()) + " kg"
                    })
                    .toArray(Object[][]::new);
        }

        // Method untuk menerima permintaan pickup
        public boolean terimaPermintaan(LokasiDropbox selectedDropbox) {
            try {
                if (currentAssignment != null) {
                    if (selectedDropbox != null) {
                        currentAssignment.setDropboxId(selectedDropbox.getId());
                    }
                    if (!currentAssignment.getStatus().equals("In Progress")) {
                        currentAssignment.setStatus("In Progress");
                        pickupController.updateAssignment(currentAssignment);
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        // Method untuk menolak permintaan pickup
        public boolean tolakPermintaan(String reason) {
            try {
                if (currentAssignment != null && reason != null && !reason.trim().isEmpty()) {
                    if (currentAssignment.getStatus().equals("Assigned")) {
                        currentAssignment.setStatus("Ditolak - " + reason);
                        currentAssignment.setNotes(reason);
                        pickupController.updateAssignment(currentAssignment);
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        // Method untuk mengecek status assignment
        public boolean hasActiveAssignment() {
            return currentAssignment != null &&
                    currentAssignment.getStatus().equals("Assigned");
        }

        // Method untuk mendapatkan semua pending pickups
        public List<PickupAssignment> getPendingPickups() {
            return pickupController.getAssignmentsByStatus("Assigned");
        }

        public void setCurrentPermintaan(PermintaanPenjemputan permintaan) {
            this.currentPermintaan = permintaan;
        }

        public void setCurrentAssignment(PickupAssignment assignment) {
            this.currentAssignment = assignment;
        }

        public void cleanup() {
            if (pickupController != null) {
                pickupController.cleanup();
            }
        }

        public LokasiDropboxController getDropboxController() {
            return dropboxController;
        }

        public PickupAssignment getCurrentAssignment() {
            return currentAssignment;
        }

        public boolean markAssignmentAsCompleted(int assignmentId) {
            PickupAssignmentController pickupAssignmentController = new PickupAssignmentController();
            return pickupAssignmentController.markAsCompleted(assignmentId);
        }


    }