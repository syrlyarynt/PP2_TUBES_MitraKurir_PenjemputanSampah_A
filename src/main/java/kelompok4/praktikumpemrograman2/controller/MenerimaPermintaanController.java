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

    // Setter untuk data yang dikirim dari MelihatPermintaanView
    public void setCurrentPermintaan(PermintaanPenjemputan permintaan) {
        this.currentPermintaan = permintaan;
    }

    public void setCurrentAssignment(PickupAssignment assignment) {
        this.currentAssignment = assignment;
    }

    // Method untuk mendapatkan data pickup yang akan ditampilkan
    public Object[][] getPickupTableData() {
        // Mengambil data permintaan yang terbaru
        List<PermintaanPenjemputan> permintaanList = permintaanController.getAllPermintaan();
        
        // Mengonversi data permintaan menjadi format yang cocok untuk tabel
        return permintaanList.stream()
                .map(permintaan -> new Object[] {
                        permintaan.getNamaPelanggan(),
                        permintaan.getAlamat(),
                        permintaan.getBerat().toString(),
                        permintaan.getHarga() != null ? permintaan.getHarga().toString() : "0"
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
                currentAssignment.setStatus("In Progress");
                pickupController.updateAssignment(currentAssignment);
                return true;
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
                currentAssignment.setStatus("Cancelled");
                currentAssignment.setNotes(reason);
                pickupController.updateAssignment(currentAssignment);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method untuk mengecek status assignment
    public boolean hasActiveAssignment() {
        // Cek current assignment atau pending pickups
        if (currentAssignment != null) {
            return true;
        }
        List<PickupAssignment> pendingAssignments = getPendingPickups();
        return !pendingAssignments.isEmpty();
    }

    // Method untuk mendapatkan semua pending pickups
    public List<PickupAssignment> getPendingPickups() {
        return pickupController.getAssignmentsByStatus("Assigned");
    }

    // Method untuk cleanup
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


}