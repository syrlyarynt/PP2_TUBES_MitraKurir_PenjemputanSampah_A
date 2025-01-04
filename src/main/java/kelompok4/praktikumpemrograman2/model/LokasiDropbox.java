package kelompok4.praktikumpemrograman2.model;

import java.math.BigDecimal;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
public class LokasiDropbox {
    private int id;
    private String nama_dropbox;
    private String alamat;
    private Double jarak;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public String getNamaDropbox() { return nama_dropbox; }
    public void setNamaDropbox(String namaDropbox) { this.nama_dropbox = namaDropbox; }


    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }


    public Double getJarak() { return jarak; }
    public void setJarak(Double jarak) { this.jarak = jarak; }

}
