package kelompok4.praktikumpemrograman2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import kelompok4.praktikumpemrograman2.controller.JenisKategoriController;
import kelompok4.praktikumpemrograman2.model.JenisKategori;

public class JenisDanKategoriView {
    private JPanel panel;
    private JenisKategoriController controller;
    private final String ICON_FOLDER = "src/main/resources/icons/";

    public JenisDanKategoriView(JenisKategoriController controller) {
        this.controller = controller;
        panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 250, 240)); // Cream background
        initComponents();
    }

    private void initComponents() {
        // Panel untuk judul
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("Jenis dan Kategori Sampah", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 0, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 240));

        // List Panel
        DefaultListModel<KategoriItem> listModel = new DefaultListModel<>();
        loadKategoriData(listModel);
        JList<KategoriItem> list = new JList<>(listModel);
        list.setCellRenderer(new CustomCellRenderer());
        list.setBackground(new Color(255, 239, 213));

        JScrollPane scrollPane = new JScrollPane(list);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(mainPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 250, 240));
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton refreshButton = new JButton("Refresh");

        //style masing btns
        addButton.setBackground(new Color(255, 160, 122));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        deleteButton.setBackground(new Color(255, 160, 122));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        updateButton.setBackground(new Color(255, 160, 122));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        refreshButton.setBackground(new Color(255, 160, 122));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));


        addButton.addActionListener(e -> onAdd(listModel));
        deleteButton.addActionListener(e -> onDelete(list.getSelectedValue(), listModel));
        updateButton.addActionListener(e -> onUpdate(list.getSelectedValue(), listModel));
        refreshButton.addActionListener(e -> onRefresh(listModel));


        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
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

    private void loadKategoriData(DefaultListModel<KategoriItem> listModel) {
        listModel.clear();
        for (JenisKategori kategori : controller.getAllKategori()) {
            String iconPath = ICON_FOLDER + kategori.getIcon();
            File iconFile = new File(iconPath);

            if (iconFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(iconPath);
                listModel.addElement(new KategoriItem(kategori.getId(), kategori.getNama(), iconPath, scaleIcon(originalIcon, 30)));
            } else {
                listModel.addElement(new KategoriItem(kategori.getId(), kategori.getNama(), null, null));
            }
        }
    }

    private void onAdd(DefaultListModel<KategoriItem> listModel) {
        String name = JOptionPane.showInputDialog(panel, "Enter category name:");
        if (name != null && !name.isEmpty()) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    String fileName = selectedFile.getName();
                    File targetFile = new File(ICON_FOLDER + fileName);
                    Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    JenisKategori newKategori = new JenisKategori();
                    newKategori.setNama(name);
                    newKategori.setIcon(fileName);

                    controller.insertKategori(newKategori);
                    listModel.addElement(new KategoriItem(newKategori.getId(), name, ICON_FOLDER + fileName, scaleIcon(new ImageIcon(targetFile.getPath()), 30)));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error adding category: " + ex.getMessage());
                }
            }
        }
    }


    private void onDelete(KategoriItem selectedItem, DefaultListModel<KategoriItem> listModel) {
        if (selectedItem != null) {
            int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure to delete " + selectedItem.name + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteKategori(selectedItem.id);
                listModel.removeElement(selectedItem);
            }
        }
    }

    private void onUpdate(KategoriItem selectedItem, DefaultListModel<KategoriItem> listModel) {
        if (selectedItem != null) {
            String newName = JOptionPane.showInputDialog(panel, "Enter new name:", selectedItem.name);
            if (newName != null && !newName.isEmpty()) {
                selectedItem.name = newName;
                JenisKategori updatedKategori = new JenisKategori();
                updatedKategori.setId(selectedItem.id);
                updatedKategori.setNama(newName);
                updatedKategori.setIcon(selectedItem.iconPath != null ? new File(selectedItem.iconPath).getName() : null);
                controller.updateKategori(updatedKategori);
                listModel.set(listModel.indexOf(selectedItem), selectedItem);
            }
        }
    }

    private void onRefresh(DefaultListModel<KategoriItem> listModel) {
        loadKategoriData(listModel);
    }

    public JPanel getPanel() {
        return panel;
    }

    private static class KategoriItem {
        int id;
        String name;
        String iconPath;
        ImageIcon icon;

        public KategoriItem(int id, String name, String iconPath, ImageIcon icon) {
            this.id = id;
            this.name = name;
            this.iconPath = iconPath;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class CustomCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof KategoriItem) {
                KategoriItem item = (KategoriItem) value;
                label.setIcon(item.icon);
                label.setText(item.name);

                if (!isSelected) {
                    if (index % 2 == 0) {
                        label.setBackground(new Color(255, 250, 240)); // Krem untuk baris genap
                    } else {
                        label.setBackground(new Color(255, 239, 213)); // Oranye muda untuk baris ganjil
                    }
                }
            }
            return label;
        }
    }
}