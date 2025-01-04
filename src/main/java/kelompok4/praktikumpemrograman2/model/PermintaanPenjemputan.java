package kelompok4.praktikumpemrograman2.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PermintaanPenjemputan {
    private int idPermintaan;
    private String namaPelanggan;
    private String alamat;
    private String kategoriSampah;
    private BigDecimal berat;
    private BigDecimal harga;
    private LocalDateTime waktuPermintaan;
    private String status;
    private Integer dropboxId;
    private LokasiDropbox lokasiDropbox; //join ke dropbox

    // Getters and Setters
    public int getIdPermintaan() { return idPermintaan; }
    public void setIdPermintaan(int idPermintaan) { this.idPermintaan = idPermintaan; }

    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getKategoriSampah() { return kategoriSampah; }
    public void setKategoriSampah(String kategoriSampah) { this.kategoriSampah = kategoriSampah; }

    public BigDecimal getBerat() { return berat; }
    public void setBerat(BigDecimal berat) { this.berat = berat; }

    public BigDecimal getHarga() { return harga; }
    public void setHarga(BigDecimal harga) { this.harga = harga; }

    public LocalDateTime getWaktuPermintaan() { return waktuPermintaan; }
    public void setWaktuPermintaan(LocalDateTime waktuPermintaan) { this.waktuPermintaan = waktuPermintaan; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getDropboxId() { return dropboxId; }
    public void setDropboxId(Integer dropboxId) { this.dropboxId = dropboxId; }

    public LokasiDropbox getLokasiDropbox() { return lokasiDropbox; }
    public void setLokasiDropbox(LokasiDropbox lokasiDropbox) { this.lokasiDropbox = lokasiDropbox; }
    @Override
    public String toString() {
        return "PermintaanPenjemputan [idPermintaan=" + idPermintaan + ", namaPelanggan=" + namaPelanggan + ", alamat="
                + alamat + ", kategoriSampah=" + kategoriSampah + ", berat=" + berat + ", harga=" + harga
                + ", waktuPermintaan=" + waktuPermintaan + ", status=" + status + ", dropboxId=" + dropboxId + "]";
    }
    
}