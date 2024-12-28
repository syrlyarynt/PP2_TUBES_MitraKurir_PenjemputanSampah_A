package kelompok4.praktikumpemrograman2;

//Java
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;

import kelompok4.praktikumpemrograman2.view.JenisDanKategori;
import kelompok4.praktikumpemrograman2.view.MelihatPermintaanView;
import kelompok4.praktikumpemrograman2.view.MenerimaPermintaanView;
import kelompok4.praktikumpemrograman2.view.LokasiDropbox;
import kelompok4.praktikumpemrograman2.view.TotalSampah;
import kelompok4.praktikumpemrograman2.view.HistoryPenjemputan;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainFrame {
    public static void main(String[] args) {
        //Intellij Theme
        FlatGrayIJTheme.setup();

        // Buat Main Frame
        JFrame frame = new JFrame("Penjemputan Sampah");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        // Buat JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tabs

        tabbedPane.addTab("Jenis dan Kategori", new JenisDanKategori().getPanel());
        tabbedPane.addTab("Permintaan Penjemputan", new MelihatPermintaanView().getPanel());
        tabbedPane.addTab("Menerima Permintaan Penjemputan", new MenerimaPermintaanView().getPanel());
        tabbedPane.addTab("Lokasi Dropbox", new LokasiDropbox().getPanel());
        tabbedPane.addTab("Total Sampah", new TotalSampah().getPanel());
        tabbedPane.addTab("History Penjemputan", new HistoryPenjemputan().getPanel());
        
        // Masukin ke frame
        frame.add(tabbedPane);

        // Show Frame
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
