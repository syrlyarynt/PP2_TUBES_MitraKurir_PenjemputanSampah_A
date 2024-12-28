package kelompok4.praktikumpemrograman2;

//Java
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;

import kelompok4.praktikumpemrograman2.view.ContohDua;
import kelompok4.praktikumpemrograman2.view.ContohEmpat;
import kelompok4.praktikumpemrograman2.view.ContohSatu;
import kelompok4.praktikumpemrograman2.view.ContohTiga;
import kelompok4.praktikumpemrograman2.view.HistoryPenjemputan;
import kelompok4.praktikumpemrograman2.view.TotalSampah;

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
<<<<<<< HEAD
        tabbedPane.addTab("Jenis dan Kategori", new JenisDanKategori().getPanel());
        tabbedPane.addTab("Permintaan Penjemputan", new ContohDua().getPanel());
        tabbedPane.addTab("Menerima Prmintaan Penjemputan", new ContohTiga().getPanel());
        tabbedPane.addTab("Lokasi Dropbox", new LokasiDropbox().getPanel());
=======
        tabbedPane.addTab("Jenis dan Kategori", new ContohSatu().getPanel());
        tabbedPane.addTab("Permintaan Penjemputan", new MelihatPermintaanView().getPanel());
        tabbedPane.addTab("Menerima Permintaan Penjemputan", new MenerimaPermintaanView().getPanel());
        tabbedPane.addTab("Lokasi Dropbox", new ContohEmpat().getPanel());
>>>>>>> dev_Zuhdi
        tabbedPane.addTab("Total Sampah", new ContohLima().getPanel());
        tabbedPane.addTab("History Penjemputan", new ContohEnam().getPanel());
        
        // Masukin ke frame
        frame.add(tabbedPane);

        // Show Frame
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
