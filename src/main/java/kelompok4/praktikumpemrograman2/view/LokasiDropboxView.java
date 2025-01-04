package kelompok4.praktikumpemrograman2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import kelompok4.praktikumpemrograman2.controller.LokasiDropboxController;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;

class AlternatingRowColorRenderer extends DefaultTableCellRenderer {
    private final Color evenRowColor = new Color(255, 250, 240); // cream
    private final Color oddRowColor = new Color(255, 239, 213);  // light peach

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (!isSelected) {
            Color background = (row % 2 == 0) ? evenRowColor : oddRowColor;
            component.setBackground(background);
            setBackground(background);
        }

        setHorizontalAlignment(SwingConstants.CENTER);
        
        return component;
    }
}

public class LokasiDropboxView {
    private final LokasiDropboxController dropboxController;
    private final JPanel panel;
    private JTable tableDropbox;

    public LokasiDropboxView() {
        dropboxController = new LokasiDropboxController();
        panel = new JPanel(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // Panel untuk judul
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("Lokasi Dropbox", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 0, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Panel for dropbox list
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Nama Dropbox", "Alamat", "Jarak", "Kapasitas Max", "Kapasitas Terisi"};
        tableDropbox = new JTable();
        tableDropbox.setModel(new DefaultTableModel(new Object[0][0], columnNames));

        // Set table styles
        tableDropbox.getTableHeader().setBackground(new Color(255, 160, 122));
        tableDropbox.setBackground(new Color(255, 250, 240));
        tableDropbox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTableHeader header = tableDropbox.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setForeground(new Color(255, 255, 255));

        // Set the alternating row color renderer
        AlternatingRowColorRenderer alternatingRenderer = new AlternatingRowColorRenderer();
        for (int i = 0; i < tableDropbox.getColumnCount(); i++) {
            tableDropbox.getColumnModel().getColumn(i).setCellRenderer(alternatingRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableDropbox);
        scrollPane.setPreferredSize(new Dimension(1000, 800));
        tableDropbox.setFillsViewportHeight(true);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        // Button Panel for Add, Edit, Delete
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 250, 240));
        JButton btnAdd = new JButton("Add Dropbox");
        JButton btnEdit = new JButton("Edit Dropbox");
        JButton btnDelete = new JButton("Delete Dropbox");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Action Listeners for Buttons
        btnAdd.addActionListener(e -> showAddDropboxDialog());
        btnEdit.addActionListener(e -> showEditDropboxDialog());
        btnDelete.addActionListener(e -> deleteDropbox());

        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadDropboxData();
    }

    private void loadDropboxData() {
        List<LokasiDropbox> dropboxes = dropboxController.getAllDropbox();
        DefaultTableModel model = (DefaultTableModel) tableDropbox.getModel();
        model.setRowCount(0);  // Clear previous data

        for (LokasiDropbox dropbox : dropboxes) {
            model.addRow(new Object[] {
                    dropbox.getNamaDropbox(),
                    dropbox.getAlamat(),
                    dropbox.getJarak(),
                    dropbox.getKapasitasMax(),
                    dropbox.getKapasitasTerisi()
            });
        }
    }

    private void showAddDropboxDialog() {
        LokasiDropbox newDropbox = showDropboxDialog(null);
        if (newDropbox != null) {
            dropboxController.createDropbox(newDropbox);
            loadDropboxData();  // Reload data after adding
        }
    }

    private void showEditDropboxDialog() {
        int selectedRow = tableDropbox.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a Dropbox to edit.");
            return;
        }

        String namaDropbox = (String) tableDropbox.getValueAt(selectedRow, 0);
        List<LokasiDropbox> dropboxes = dropboxController.getAllDropbox();
        LokasiDropbox existingDropbox = null;

        for (LokasiDropbox dropbox : dropboxes) {
            if (dropbox.getNamaDropbox().equals(namaDropbox)) {
                existingDropbox = dropbox;
                break;
            }
        }

        if (existingDropbox != null) {
            LokasiDropbox updatedDropbox = showDropboxDialog(existingDropbox);
            if (updatedDropbox != null) {
                dropboxController.updateDropbox(updatedDropbox);
                loadDropboxData();  // Reload data after updating
            }
        } else {
            JOptionPane.showMessageDialog(null, "Dropbox not found.");
        }
    }

    private void deleteDropbox() {
        int selectedRow = tableDropbox.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a Dropbox to delete.");
            return;
        }

        String namaDropbox = (String) tableDropbox.getValueAt(selectedRow, 0);
        List<LokasiDropbox> dropboxes = dropboxController.getAllDropbox();
        LokasiDropbox dropboxToDelete = null;

        for (LokasiDropbox dropbox : dropboxes) {
            if (dropbox.getNamaDropbox().equals(namaDropbox)) {
                dropboxToDelete = dropbox;
                break;
            }
        }

        if (dropboxToDelete != null) {
            dropboxController.deleteDropbox(dropboxToDelete.getId());
            loadDropboxData();  // Reload data after deleting
        } else {
            JOptionPane.showMessageDialog(null, "Dropbox not found.");
        }
    }

    private LokasiDropbox showDropboxDialog(LokasiDropbox existingDropbox) {
        JTextField txtNamaDropbox = new JTextField(existingDropbox != null ? existingDropbox.getNamaDropbox() : "");
        JTextField txtAlamat = new JTextField(existingDropbox != null ? existingDropbox.getAlamat() : "");
        JTextField txtJarak = new JTextField(existingDropbox != null ? existingDropbox.getJarak().toString() : "");
        JTextField txtKapasitasMax = new JTextField(existingDropbox != null ? existingDropbox.getKapasitasMax().toString() : "");
        JTextField txtKapasitasTerisi = new JTextField(existingDropbox != null ? existingDropbox.getKapasitasTerisi().toString() : "");

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.setBackground(new Color(255, 250, 240)); // Cream background

        panel.add(new JLabel("Nama Dropbox:"));
        panel.add(txtNamaDropbox);
        panel.add(new JLabel("Alamat:"));
        panel.add(txtAlamat);
        panel.add(new JLabel("Jarak:"));
        panel.add(txtJarak);
        panel.add(new JLabel("Kapasitas Max:"));
        panel.add(txtKapasitasMax);
        panel.add(new JLabel("Kapasitas Terisi:"));
        panel.add(txtKapasitasTerisi);

        int result = JOptionPane.showConfirmDialog(null, panel, existingDropbox == null ? "Add Dropbox" : "Edit Dropbox", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                LokasiDropbox dropbox = existingDropbox != null ? existingDropbox : new LokasiDropbox();
                dropbox.setNamaDropbox(txtNamaDropbox.getText());
                dropbox.setAlamat(txtAlamat.getText());
                dropbox.setJarak(new BigDecimal(txtJarak.getText()));
                dropbox.setKapasitasMax(new BigDecimal(txtKapasitasMax.getText()));
                dropbox.setKapasitasTerisi(new BigDecimal(txtKapasitasTerisi.getText()));
                return dropbox;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
            }
        }
        return null;
    }

    public JPanel getPanel() {
        return panel;
    }
}