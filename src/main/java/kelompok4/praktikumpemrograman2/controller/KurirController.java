package kelompok4.praktikumpemrograman2.controller;

import kelompok4.praktikumpemrograman2.model.Kurir;
import kelompok4.praktikumpemrograman2.services.KurirService;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import java.util.List;

public class KurirController {
    private final KurirService service;

    public KurirController() {
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        this.service = new KurirService(sqlSession);
    }

    public List<Kurir> getAllKurir() {
        List<Kurir> list = service.getAllKurir();
        list.forEach(kurir -> System.out.println("Controller Load: " + kurir)); // Debugging
        return list;
    }

    public Kurir getKurirById(int id) {
        Kurir kurir = service.getKurirById(id);
        System.out.println("Controller: Kurir = " + kurir); // Debugging
        return kurir;
    }

    public void insertKurir(String nama, String nomorTelepon, String alamat, String status) {
        Kurir kurir = new Kurir();
        kurir.setNama(nama);
        kurir.setNomorTelepon(nomorTelepon);
        kurir.setAlamatKurir(alamat);
        kurir.setStatus(status);
        service.insertKurir(kurir);
    }

    public void updateKurir(int id, String nama, String nomorTelepon, String alamat, String status) {
        Kurir kurir = new Kurir(id, nama, nomorTelepon, alamat, status);
        System.out.println("Updating Kurir: " + kurir); // Debugging
        service.updateKurir(kurir);
        System.out.println("Kurir updated successfully"); // Debugging
    }


    public void deleteKurir(int id) {
        service.deleteKurir(id);
    }
}
