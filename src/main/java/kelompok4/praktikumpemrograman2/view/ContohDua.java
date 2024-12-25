package kelompok4.praktikumpemrograman2.view;

import javax.swing.*;
import java.awt.*;

public class ContohDua{
    private JPanel panel;

    public ContohDua() {
        panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Untuk Halaman 2", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}
