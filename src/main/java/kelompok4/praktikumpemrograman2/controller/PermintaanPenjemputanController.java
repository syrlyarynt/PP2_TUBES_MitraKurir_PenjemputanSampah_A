package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputan;
import kelompok4.praktikumpemrograman2.services.PermintaanPenjemputanService;

import java.util.List;

public class PermintaanPenjemputanController {
    private final PermintaanPenjemputanService service;

    public PermintaanPenjemputanController(PermintaanPenjemputanService service) {
        // SqlSession sqlSession = MyBatisUtil.getSqlSession();
        this.service = service;
    }

    public List<PermintaanPenjemputan> getAllPermintaan() {
        return service.getAllPermintaan();
    }

    public PermintaanPenjemputan getPermintaanById(int id) {
        return service.getPermintaanById(id);
    }

    public void createPermintaan(PermintaanPenjemputan permintaan) {
        service.createPermintaan(permintaan);
    }

    public void updatePermintaan(PermintaanPenjemputan permintaan) {
        service.updatePermintaan(permintaan);
    }

    public void deletePermintaan(int id) {
        service.deletePermintaan(id);
    }

}



