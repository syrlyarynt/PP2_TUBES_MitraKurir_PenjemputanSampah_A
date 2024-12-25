package kelompok4.praktikumpemrograman2.view;

import javax.swing.*;
import java.awt.*;

public class ContohEnam{
    private JPanel panel;

    public ContohEnam() {
        panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Untuk Halaman 6", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}
