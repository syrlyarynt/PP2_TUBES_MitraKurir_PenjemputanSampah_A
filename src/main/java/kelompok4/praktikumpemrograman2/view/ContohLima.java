package kelompok4.praktikumpemrograman2.view;

import javax.swing.*;
import java.awt.*;

public class ContohLima{
    private JPanel panel;

    public ContohLima() {
        panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Untuk Halaman 5", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}
