package kelompok4.praktikumpemrograman2.view;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatLightLaf;

public class JenisDanKategori {
    private JPanel panel;

    // Class text & icon
    public static class KategoriItem {
        String name;
        ImageIcon icon;

        public KategoriItem(String name, ImageIcon icon) {
            this.name = name;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private ImageIcon scaleIcon(ImageIcon icon, int height) {
        Image img = icon.getImage();
        int originalWidth = img.getWidth(null);
        int originalHeight = img.getHeight(null);
        if (originalWidth > 0 && originalHeight > 0) {
            int width = (int) ((double) originalWidth / originalHeight * height);
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        }
        return icon;
    }

    public JenisDanKategori() {
        UIManager.put( "Button.arc", 999 );

        panel = new JPanel(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout("", "[grow]", "[][][grow]"));
        
        JLabel titleLabel = new JLabel("Jenis dan Kategori Sampah");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        Font defaultFont = titleLabel.getFont(); 
        titleLabel.setFont(defaultFont.deriveFont(18f));
        mainPanel.add(titleLabel, "span, align center, wrap");

        DefaultListModel<KategoriItem> listModel = new DefaultListModel<>();
        listModel.addElement(new KategoriItem("Peralatan Rumah Tangga", new ImageIcon(getClass().getResource("/icons/house.png"))));
        listModel.addElement(new KategoriItem("Perangkat Komputer", new ImageIcon(getClass().getResource("/icons/computer.png"))));
        listModel.addElement(new KategoriItem("Perangkat Komunikasi", new ImageIcon(getClass().getResource("/icons/smartphone.png"))));
        listModel.addElement(new KategoriItem("Perangkat Musik", new ImageIcon(getClass().getResource("/icons/live-music.png"))));
        listModel.addElement(new KategoriItem("Perangkat Elektronik Medis", new ImageIcon(getClass().getResource("/icons/syringe.png"))));
        listModel.addElement(new KategoriItem("Peralatan Pencahayaan", new ImageIcon(getClass().getResource("/icons/light-bulb.png"))));

        JList<KategoriItem> list = new JList<>(listModel);

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof KategoriItem) {
                    KategoriItem item = (KategoriItem) value;
                    label.setIcon(scaleIcon(item.icon, 30));
                    label.setText(item.name);
                    Font defaultFont = label.getFont(); 
                    label.setFont(defaultFont.deriveFont(20f));
                    label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    
                    if (isSelected) {
                        label.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(Color.WHITE, 2),
                                BorderFactory.createEmptyBorder(10, 10, 10, 10)
                        ));
                    }
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(1000, 700)); 
        mainPanel.add(scrollPane, "span, grow, wrap");

        JButton Button = new JButton("Pilih");
        Button.setPreferredSize(new Dimension(200, 80)); 
        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // aksi button "Pilih"
            }
        });
        mainPanel.add(Button, "span, align center");

        panel.add(mainPanel, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}
