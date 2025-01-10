package kelompok4.praktikumpemrograman2.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class History {
    private int idRiwayat;
    private LocalDateTime waktuSelesai;
    private String lokasi;
    private int kategoriSampah;
    private BigDecimal beratSampah;
    private BigDecimal harga;
    private String statusPenyelesaian;
    private Integer pickupAssignmentId;
    private PickupAssignment pickupAssignment;

    // Getters and Setters
    public int getIdRiwayat() { return idRiwayat; }
    public void setIdRiwayat(int idRiwayat) { this.idRiwayat = idRiwayat; }

    public LocalDateTime getWaktuSelesai() { return waktuSelesai; }
    public void setWaktuSelesai(LocalDateTime waktuSelesai) { this.waktuSelesai = waktuSelesai; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public void setKategoriSampah(int kategoriSampah) {  // Changed to accept int
        this.kategoriSampah = kategoriSampah;
    }

    public int getKategoriSampah() {  // Changed to return int
        return kategoriSampah;
    }
    public BigDecimal getBeratSampah() { return beratSampah; }
    public void setBeratSampah(BigDecimal beratSampah) { this.beratSampah = beratSampah; }

    public BigDecimal getHarga() { return harga; }
    public void setHarga(BigDecimal harga) { this.harga = harga; }

    public String getStatusPenyelesaian() { return statusPenyelesaian; }
    public void setStatusPenyelesaian(String statusPenyelesaian) { this.statusPenyelesaian = statusPenyelesaian; }

    public Integer getPickupAssignmentId() {
        return pickupAssignmentId;
    }

    public void setPickupAssignmentId(Integer pickupAssignmentId) {
        this.pickupAssignmentId = pickupAssignmentId;
    }

    public PickupAssignment getPickupAssignment() {
        return pickupAssignment;
    }

    public void setPickupAssignment(PickupAssignment pickupAssignment) {
        this.pickupAssignment = pickupAssignment;
    }


}
