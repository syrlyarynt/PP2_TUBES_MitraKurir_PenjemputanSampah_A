package kelompok4.praktikumpemrograman2.view;

import javax.swing.*;
import java.awt.*;

public class MenerimaPermintaanView{
    private JPanel panel;

    public MenerimaPermintaanView() {
        panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Untuk Halaman 3", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}
