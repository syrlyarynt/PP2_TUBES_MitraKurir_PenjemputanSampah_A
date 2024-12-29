package kelompok4.praktikumpemrograman2.model;

import java.math.BigDecimal;

public class LokasiDropbox {
    private int id;
    private String namaDropbox;
    private String alamat;
    private BigDecimal jarak;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaDropbox() { return namaDropbox; }
    public void setNamaDropbox(String namaDropbox) { this.namaDropbox = namaDropbox; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public BigDecimal getJarak() { return jarak; }
    public void setJarak(BigDecimal jarak) { this.jarak = jarak; }
}
