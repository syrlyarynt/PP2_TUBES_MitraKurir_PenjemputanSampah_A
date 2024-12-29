package kelompok4.praktikumpemrograman2.view;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import kelompok4.praktikumpemrograman2.controller.JenisKategoriController;
import kelompok4.praktikumpemrograman2.model.JenisKategori;

public class JenisDanKategori {
    private JPanel panel;
    private JenisKategoriController controller;
    private final String ICON_FOLDER = "src/main/resources/icons/";

    public JenisDanKategori(JenisKategoriController controller) {
        this.controller = controller;
        panel = new JPanel(new BorderLayout());
        JPanel mainPanel = new JPanel(new MigLayout("", "[grow]", "[][][grow][]"));

        JLabel titleLabel = new JLabel("Jenis dan Kategori Sampah");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        mainPanel.add(titleLabel, "span, align center, wrap");

        DefaultListModel<KategoriItem> listModel = new DefaultListModel<>();
        loadKategoriData(listModel);
        JList<KategoriItem> list = new JList<>(listModel);
        list.setCellRenderer(new CustomCellRenderer());

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        mainPanel.add(scrollPane, "span, grow, wrap");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> onAdd(listModel));
        deleteButton.addActionListener(e -> onDelete(list.getSelectedValue(), listModel));
        updateButton.addActionListener(e -> onUpdate(list.getSelectedValue(), listModel));
        refreshButton.addActionListener(e -> onRefresh(listModel));

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(buttonPanel, "span, wrap");
        panel.add(mainPanel, BorderLayout.CENTER);
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
                System.out.println("Loaded: ID=" + kategori.getId() + ", Name=" + kategori.getNama() + ", Icon Path=" + iconPath);
            } else {
                System.out.println("Warning: Icon file not found for ID=" + kategori.getId() + ", Icon Path=" + iconPath);
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
                    System.out.println("Added: Name=" + name + ", Icon=" + fileName);
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
                System.out.println("Deleted: ID=" + selectedItem.id + ", Name=" + selectedItem.name);
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
                System.out.println("Updated: ID=" + selectedItem.id + ", New Name=" + newName);
            }
        }
    }

    private void onRefresh(DefaultListModel<KategoriItem> listModel) {
        loadKategoriData(listModel);
        System.out.println("Refreshed category list.");
    }

    private void printListContents(DefaultListModel<KategoriItem> listModel) {
        System.out.println("Current List Contents:");
        for (int i = 0; i < listModel.size(); i++) {
            KategoriItem item = listModel.getElementAt(i);
            System.out.println("  - ID=" + item.id + ", Name=" + item.name + ", Icon Path=" + (item.iconPath != null ? item.iconPath : "null"));
        }
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
            }
            return label;
        }
    }
}
