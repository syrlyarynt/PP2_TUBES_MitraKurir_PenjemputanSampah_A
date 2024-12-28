package kelompok4.praktikumpemrograman2;

//Java
import javax.swing.*;
import java.awt.*;

import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;

//File Path
import kelompok4.praktikumpemrograman2.view.*;

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
        tabbedPane.addTab("Permintaan Penjemputan", new ContohDua().getPanel());
        tabbedPane.addTab("Menerima Prmintaan Penjemputan", new ContohTiga().getPanel());
        tabbedPane.addTab("Lokasi Dropbox", new LokasiDropbox().getPanel());
        tabbedPane.addTab("Total Sampah", new ContohLima().getPanel());
        tabbedPane.addTab("History Penjemputan", new ContohEnam().getPanel());

        // Masukin ke frame
        frame.add(tabbedPane);

        // Show Frame
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
