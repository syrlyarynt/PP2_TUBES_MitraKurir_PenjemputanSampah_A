package kelompok4.praktikumpemrograman2.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import kelompok4.praktikumpemrograman2.controller.LokasiDropboxController;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import net.miginfocom.swing.MigLayout;

import java.math.BigDecimal;
import java.util.List;

public class LokasiDropboxView {
    private final LokasiDropboxController dropboxController;
    private final JPanel panel;
    private JTable tableDropbox;

    public LokasiDropboxView() {
        dropboxController = new LokasiDropboxController();
        panel = new JPanel(new MigLayout("wrap 2", "[grow, fill][grow, fill]", "[]10[]10[]10[]10[]10[]10[grow]"));
        initComponents();
    }

    private void initComponents() {
        // Panel for title
        JLabel titleLabel = new JLabel("Lokasi Dropbox", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 0, 0));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 250, 240));
        panel.add(titleLabel, "span, align center");

        // Panel for dropbox list
        String[] columnNames = {"Nama Dropbox", "Alamat", "Jarak", "Kapasitas Max", "Kapasitas Terisi"};
        tableDropbox = new JTable();
        tableDropbox.setModel(new DefaultTableModel(new Object[0][0], columnNames));

        // Set table styles
        tableDropbox.setBackground(new Color(255, 239, 213));
        tableDropbox.getTableHeader().setBackground(new Color(255, 160, 122));
        tableDropbox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTableHeader header = tableDropbox.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setForeground(new Color(255, 255, 255));

        // Center-align text in each column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableDropbox.getColumnCount(); i++) {
            tableDropbox.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableDropbox);
        scrollPane.setPreferredSize(new Dimension(1000, 800));
        tableDropbox.setFillsViewportHeight(true);
        panel.add(scrollPane, "cell 0 6 2 1");

        // Button Panel for Add, Edit, Delete
        JPanel buttonPanel = new JPanel();
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

        panel.add(buttonPanel, "cell 0 7 2 1");

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
