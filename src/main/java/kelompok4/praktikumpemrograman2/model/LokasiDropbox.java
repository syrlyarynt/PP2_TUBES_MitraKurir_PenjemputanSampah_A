package kelompok4.praktikumpemrograman2.model;

import java.math.BigDecimal;
import java.util.List;

public class LokasiDropbox {
    private int id;
    private String namaDropbox;
    private String alamat;
    private BigDecimal jarak;
    private BigDecimal kapasitasMax; // New field
    private BigDecimal kapasitasTerisi; // New field
    private List<DropboxRate> rates; // New relationship

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaDropbox() { return namaDropbox; }
    public void setNamaDropbox(String namaDropbox) { this.namaDropbox = namaDropbox; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public BigDecimal getJarak() { return jarak; }
    public void setJarak(BigDecimal jarak) { this.jarak = jarak; }


    public BigDecimal getKapasitasMax() {
        return kapasitasMax;
    }

    public void setKapasitasMax(BigDecimal kapasitasMax) {
        this.kapasitasMax = kapasitasMax;
    }

    public BigDecimal getKapasitasTerisi() {
        return kapasitasTerisi;
    }

    public void setKapasitasTerisi(BigDecimal kapasitasTerisi) {
        this.kapasitasTerisi = kapasitasTerisi;
    }

    public List<DropboxRate> getRates() {
        return rates;
    }

    public void setRates(List<DropboxRate> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return namaDropbox; // Tampilkan hanya namaDropbox
    }
}
