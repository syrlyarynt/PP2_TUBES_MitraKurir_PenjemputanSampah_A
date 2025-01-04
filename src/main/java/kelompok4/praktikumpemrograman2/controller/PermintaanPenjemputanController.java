package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.JenisKategori;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputan;
import kelompok4.praktikumpemrograman2.services.PermintaanPenjemputanService;
import org.apache.ibatis.session.SqlSession;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

            // Buat objek permintaan dengan nilai default yang tidak null
            PermintaanPenjemputan permintaan = new PermintaanPenjemputan();
            permintaan.setNamaPelanggan(nama);
            permintaan.setAlamat(alamat);
            permintaan.setKategoriSampahId(kategoriSampahId);
            permintaan.setBerat(BigDecimal.valueOf(berat));
            permintaan.setDropboxId(dropboxId);

            // Set nilai default untuk field yang required di database
            permintaan.setStatus("Menunggu");
            permintaan.setWaktuPermintaan(LocalDateTime.now());
            permintaan.setTotalBiaya(BigDecimal.ZERO);  // Set default 0 jika belum dihitung

            // Hitung harga default jika null (bisa disesuaikan dengan business logic)
            if (harga == null) {
                BigDecimal defaultHarga = BigDecimal.valueOf(8000); // harga default per kg
                permintaan.setHarga(defaultHarga.multiply(BigDecimal.valueOf(berat)));
            } else {
                permintaan.setHarga(harga);
            }

            // Debug log sebelum insert
            System.out.println("=== Data yang akan diinsert ===");
            System.out.println(permintaan.toString());

            // Insert dengan explicit transaction
            SqlSession session = MyBatisUtil.getSqlSession();
            try {
                service.createPermintaan(permintaan);
                session.commit();
                System.out.println("Data berhasil ditambahkan: " + permintaan);
            } catch (Exception e) {
                session.rollback();
                throw e;
            } finally {
                session.close();
            }

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
