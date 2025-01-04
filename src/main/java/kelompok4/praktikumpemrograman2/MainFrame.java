package kelompok4.praktikumpemrograman2;

import javax.swing.*;

import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;

import kelompok4.praktikumpemrograman2.view.JenisDanKategori;
import kelompok4.praktikumpemrograman2.view.PermintaanPenjemputanView;
import kelompok4.praktikumpemrograman2.view.MenerimaPermintaanView;
import kelompok4.praktikumpemrograman2.view.LokasiDropboxView;
import kelompok4.praktikumpemrograman2.view.TotalSampah;
import kelompok4.praktikumpemrograman2.view.HistoryPenjemputan;
import kelompok4.praktikumpemrograman2.controller.JenisKategoriController;
import kelompok4.praktikumpemrograman2.services.JenisKategoriService;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class MainFrame {
    private static SqlSession sqlSession;

    public static void main(String[] args) {
        FlatGrayIJTheme.setup();

        JFrame frame = new JFrame("Penjemputan Sampah");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        JTabbedPane tabbedPane = new JTabbedPane();

        try {
            sqlSession = MyBatisUtil.getSqlSession();
            JenisKategoriService service = new JenisKategoriService(sqlSession);
            JenisKategoriController controller = new JenisKategoriController(service);

            tabbedPane.addTab("Jenis dan Kategori", new JenisDanKategori(controller).getPanel());
            tabbedPane.addTab("Permintaan Penjemputan", new PermintaanPenjemputanView().getPanel());
            tabbedPane.addTab("Menerima Permintaan Penjemputan", new MenerimaPermintaanView().getPanel());
            tabbedPane.addTab("Lokasi Dropbox", new LokasiDropboxView().getPanel());
            tabbedPane.addTab("Total Sampah", new TotalSampah().getPanel());
            tabbedPane.addTab("History Penjemputan", new HistoryPenjemputan().getPanel());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error initializing database connection: " + e.getMessage());
            return;
        }

        frame.add(tabbedPane);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }));
    }
}
