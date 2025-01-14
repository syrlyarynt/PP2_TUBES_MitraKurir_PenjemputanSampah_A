package kelompok4.praktikumpemrograman2.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TotalSampah {
    private int id;
    private LocalDate tanggal;
    private String jenisSampah;
    private BigDecimal totalBerat;
    private BigDecimal totalHarga;
    private BigDecimal beratKg;
    private Integer permintaanId;
    private String namaJenis;
    private String namaPelanggan;
    private String alamat;
    private String kategoriSampah;
    private String waktuPermintaan;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWaktuPermintaan() {
        return waktuPermintaan;
    }

    public void setWaktuPermintaan(String waktuPermintaan) {
        this.waktuPermintaan = waktuPermintaan;
    }

    public String getKategoriSampah() {
        return kategoriSampah;
    }

    public void setKategoriSampah(String kategoriSampah) {
        this.kategoriSampah = kategoriSampah;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    // Getter
    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    // Setter
    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getNamaJenis() { return namaJenis; }
    public void setNamaJenis(String namaJenis) { this.namaJenis = namaJenis; }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public String getJenisSampah() { return jenisSampah; }
    public void setJenisSampah(String jenisSampah) { this.jenisSampah = jenisSampah; }

    public BigDecimal getTotalBerat() { return totalBerat; }
    public void setTotalBerat(BigDecimal totalBerat) { this.totalBerat = totalBerat; }

    public BigDecimal getTotalHarga() { return totalHarga; }
    public void setTotalHarga(BigDecimal totalHarga) { this.totalHarga = totalHarga; }

    public BigDecimal getBeratKg() { return beratKg; }
    public void setBeratKg(BigDecimal beratKg) { this.beratKg = beratKg; }

    public Integer getPermintaanId() { return permintaanId; }
    public void setPermintaanId(Integer permintaanId) { this.permintaanId = permintaanId; }

    @Override
    public String toString() {
        return "TotalSampah{" +
                "id=" + id +
                ", tanggal=" + tanggal +
                ", jenisSampah='" + jenisSampah + '\'' +
                ", beratKg=" + beratKg +
                ", namaJenis='" + namaJenis + '\'' +
                '}';
    }
}
