package kelompok4.praktikumpemrograman2.view;

import javax.swing.*;
import java.awt.*;

public class ContohEmpat{
    private JPanel panel;

    public ContohEmpat() {
        panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Untuk Halaman 4", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}
