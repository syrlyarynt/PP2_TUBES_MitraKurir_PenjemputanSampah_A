package kelompok4.praktikumpemrograman2.view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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

