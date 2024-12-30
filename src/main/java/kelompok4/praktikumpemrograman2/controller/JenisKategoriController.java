package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.JenisKategori;
import kelompok4.praktikumpemrograman2.services.JenisKategoriService;

import java.util.List;

public class JenisKategoriController {
    private final JenisKategoriService jenisKategoriService;

    public JenisKategoriController(JenisKategoriService jenisKategoriService) {
        this.jenisKategoriService = jenisKategoriService;
    }

    public List<JenisKategori> getAllKategori() {
        System.out.println("Fetching all categories...");
        List<JenisKategori> kategoriList = jenisKategoriService.getAllKategori();
        System.out.println("Fetched " + kategoriList.size() + " categories.");
        return kategoriList;
    }

    public JenisKategori getKategoriById(int id) {
        System.out.println("Fetching category with ID: " + id);
        JenisKategori kategori = jenisKategoriService.getKategoriById(id);
        System.out.println("Fetched category: " + (kategori != null ? kategori.getNama() : "Not Found"));
        return kategori;
    }

    public void insertKategori(JenisKategori kategori) {
        System.out.println("Inserting category: " + kategori.getNama());
        jenisKategoriService.createKategori(kategori);
        System.out.println("Category inserted successfully.");
    }

    public void updateKategori(JenisKategori kategori) {
        System.out.println("Updating category with ID: " + kategori.getId());
        jenisKategoriService.updateKategori(kategori);
        System.out.println("Category updated successfully.");
    }

    public void deleteKategori(int id) {
        System.out.println("Deleting category with ID: " + id);
        jenisKategoriService.deleteKategori(id);
        System.out.println("Category deleted successfully.");
    }
}