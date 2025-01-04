package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.JenisKategori;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputan;
import kelompok4.praktikumpemrograman2.services.PermintaanPenjemputanService;
import org.apache.ibatis.session.SqlSession;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;

import java.math.BigDecimal;
import java.util.List;

public class PermintaanPenjemputanController {
    private final PermintaanPenjemputanService service;
    private final LokasiDropboxController dropboxController;

    public PermintaanPenjemputanController() {
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        this.service = new PermintaanPenjemputanService(sqlSession);
        this.dropboxController = new LokasiDropboxController();
    }

    public List<PermintaanPenjemputan> getAllPermintaan() {
        return service.getAllPermintaan();
    }

    public PermintaanPenjemputan getPermintaanById(int id) {
        return service.getPermintaanById(id);
    }

    public void createPermintaan(String nama, String alamat, int kategoriSampahId, double berat, Integer dropboxId, BigDecimal harga) {
        try {
            // Validasi data input
            if (nama == null || nama.trim().isEmpty()) {
                throw new IllegalArgumentException("Nama pelanggan tidak boleh kosong.");
            }
            if (alamat == null || alamat.trim().isEmpty()) {
                throw new IllegalArgumentException("Alamat tidak boleh kosong.");
            }
            if (berat <= 0) {
                throw new IllegalArgumentException("Berat harus lebih dari 0.");
            }
            if (!service.isKategoriExists(kategoriSampahId)) {
                throw new IllegalArgumentException("Kategori sampah tidak valid.");
            }
            if (dropboxId != null && !service.isDropboxExists(dropboxId)) {
                throw new IllegalArgumentException("Lokasi Dropbox tidak valid.");
            }

            // Buat objek permintaan
            PermintaanPenjemputan permintaan = new PermintaanPenjemputan();
            permintaan.setNamaPelanggan(nama);
            permintaan.setAlamat(alamat);
            permintaan.setKategoriSampahId(kategoriSampahId);
            permintaan.setBerat(BigDecimal.valueOf(berat));
            permintaan.setDropboxId(dropboxId);
//            permintaan.setLokasiDropbox(lokasiDropbox);
            permintaan.setHarga(harga); // Bisa null
            permintaan.setStatus("Menunggu");

            // Insert permintaan
            service.createPermintaan(permintaan);
            System.out.println("Data berhasil ditambahkan: " + permintaan);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal membuat permintaan: " + e.getMessage());
        }
    }



    public void updatePermintaan(PermintaanPenjemputan permintaan) {
        service.updatePermintaan(permintaan);
    }

    public void deletePermintaan(int id) {
        service.deletePermintaan(id);
    }

    public List<JenisKategori> getAllKategoriSampah() {
        return service.getAllKategoriSampah();
    }

    public List<LokasiDropbox> getAllDropbox() {
        return dropboxController.getAllDropbox();
    }

    public LokasiDropboxController getDropboxController() {
        return dropboxController;
    }


}
