package kelompok4.praktikumpemrograman2.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PickupAssignment {
    private int id;
    private int permintaanId;
    private int dropboxId;
    private String status;
    private LocalDateTime pickupDate;
    private LocalDateTime completionDate;
    private BigDecimal totalWeight;
    private BigDecimal beratAktual;
    private BigDecimal totalCost;
    private BigDecimal hargaFinal;
    private String notes;

    private PermintaanPenjemputan permintaan;
    private LokasiDropbox dropbox;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPermintaanId() { return permintaanId; }
    public void setPermintaanId(int permintaanId) { this.permintaanId = permintaanId; }

    public int getDropboxId() { return dropboxId; }
    public void setDropboxId(int dropboxId) { this.dropboxId = dropboxId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDateTime pickupDate) { this.pickupDate = pickupDate; }

    public LocalDateTime getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDateTime completionDate) { this.completionDate = completionDate; }

    public BigDecimal getTotalWeight() { return totalWeight; }
    public void setTotalWeight(BigDecimal totalWeight) { this.totalWeight = totalWeight; }

    public BigDecimal getBeratAktual() { return beratAktual; }
    public void setBeratAktual(BigDecimal beratAktual) { this.beratAktual = beratAktual; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public BigDecimal getHargaFinal() { return hargaFinal; }
    public void setHargaFinal(BigDecimal hargaFinal) { this.hargaFinal = hargaFinal; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public PermintaanPenjemputan getPermintaan() { return permintaan; }
    public void setPermintaan(PermintaanPenjemputan permintaan) { this.permintaan = permintaan; }

    public LokasiDropbox getDropbox() { return dropbox; }
    public void setDropbox(LokasiDropbox dropbox) { this.dropbox = dropbox; }
}
