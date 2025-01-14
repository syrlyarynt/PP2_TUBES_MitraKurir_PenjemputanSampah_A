package kelompok4.praktikumpemrograman2.model;

public class Kurir {
    private int id;
    private String nama;
    private String nomorTelepon;
    private String alamatKurir;
    private String status;

    // Constructor
    public Kurir() {}

    public Kurir(int id, String nama, String nomorTelepon, String alamatKurir, String status) {
        this.id = id;
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
        this.alamatKurir = alamatKurir;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNomorTelepon() { return nomorTelepon; }
    public void setNomorTelepon(String nomorTelepon) { this.nomorTelepon = nomorTelepon; }

    public String getAlamatKurir() { return alamatKurir; }
    public void setAlamatKurir(String alamatKurir) { this.alamatKurir = alamatKurir; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Kurir{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", nomorTelepon='" + nomorTelepon + '\'' +
                ", alamatKurir='" + alamatKurir + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
