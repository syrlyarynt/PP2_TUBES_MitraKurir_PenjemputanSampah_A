package kelompok4.praktikumpemrograman2.view;

import javax.swing.*;
import java.awt.*;

public class ContohSatu{
    private JPanel panel;

    public ContohSatu() {
        panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Untuk Halaman 1", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}

