package kelompok4.praktikumpemrograman2.view;

import kelompok4.praktikumpemrograman2.controller.DropboxRateController;
import kelompok4.praktikumpemrograman2.controller.LokasiDropboxController;
import kelompok4.praktikumpemrograman2.model.DropboxRate;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class DropboxRateView {
    private final DropboxRateController rateController;
    private final LokasiDropboxController dropboxController;
    private final JPanel mainPanel;
    private JTable table;
    private DefaultTableModel tableModel;

    public DropboxRateView() {
        rateController = new DropboxRateController();
        dropboxController = new LokasiDropboxController();
        mainPanel = new JPanel(new BorderLayout());
        initComponents();
        loadData();
    }

    private void initComponents() {
        // Panel for Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Dropbox ID", "Min Distance", "Max Distance", "Base Rate", "Distance Rate"}, 0);
        table = new JTable(tableModel);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel for Buttons
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Add action listeners
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelectedRate());

        // Layout setup
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear table
        List<DropboxRate> rates = rateController.getAllRates();
        for (DropboxRate rate : rates) {
            tableModel.addRow(new Object[]{
                    rate.getId(),
                    rate.getDropboxId(),
                    rate.getMinDistance(),
                    rate.getMaxDistance(),
                    rate.getBaseRate(),
                    rate.getDistanceRate()
            });
        }
    }

    private void showAddDialog() {
        DropboxRate rate = showRateDialog(null);
        if (rate != null) {
            rateController.createRate(rate);
            loadData();
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to edit.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        DropboxRate existingRate = rateController.getRateById(id);
        DropboxRate updatedRate = showRateDialog(existingRate);
        if (updatedRate != null) {
            rateController.updateRate(updatedRate);
            loadData();
        }
    }

    private void deleteSelectedRate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this rate?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            rateController.deleteRate(id);
            loadData();
        }
    }

    private DropboxRate showRateDialog(DropboxRate rate) {
        // Load dropbox data
        List<LokasiDropbox> dropboxes = dropboxController.getAllDropbox();
        JComboBox<LokasiDropbox> cmbDropbox = new JComboBox<>();
        for (LokasiDropbox dropbox : dropboxes) {
            cmbDropbox.addItem(dropbox);
        }

        if (rate != null) {
            for (int i = 0; i < cmbDropbox.getItemCount(); i++) {
                if (cmbDropbox.getItemAt(i).getId() == rate.getDropboxId()) {
                    cmbDropbox.setSelectedIndex(i);
                    break;
                }
            }
        }

        JTextField txtMinDistance = new JTextField(rate != null && rate.getMinDistance() != null ? rate.getMinDistance().toString() : "");
        JTextField txtMaxDistance = new JTextField(rate != null && rate.getMaxDistance() != null ? rate.getMaxDistance().toString() : "");
        JTextField txtBaseRate = new JTextField(rate != null && rate.getBaseRate() != null ? rate.getBaseRate().toString() : "");
        JTextField txtDistanceRate = new JTextField(rate != null && rate.getDistanceRate() != null ? rate.getDistanceRate().toString() : "");

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Dropbox:"));
        panel.add(cmbDropbox);
        panel.add(new JLabel("Min Distance:"));
        panel.add(txtMinDistance);
        panel.add(new JLabel("Max Distance:"));
        panel.add(txtMaxDistance);
        panel.add(new JLabel("Base Rate:"));
        panel.add(txtBaseRate);
        panel.add(new JLabel("Distance Rate:"));
        panel.add(txtDistanceRate);

        int result = JOptionPane.showConfirmDialog(null, panel, rate == null ? "Add Rate" : "Edit Rate", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                LokasiDropbox selectedDropbox = (LokasiDropbox) cmbDropbox.getSelectedItem();
                BigDecimal minDistance = txtMinDistance.getText().isEmpty() ? null : new BigDecimal(txtMinDistance.getText());
                BigDecimal maxDistance = txtMaxDistance.getText().isEmpty() ? null : new BigDecimal(txtMaxDistance.getText());
                BigDecimal baseRate = txtBaseRate.getText().isEmpty() ? null : new BigDecimal(txtBaseRate.getText());
                BigDecimal distanceRate = txtDistanceRate.getText().isEmpty() ? null : new BigDecimal(txtDistanceRate.getText());

                if (rate == null) rate = new DropboxRate();
                rate.setDropboxId(selectedDropbox.getId());
                rate.setMinDistance(minDistance);
                rate.setMaxDistance(maxDistance);
                rate.setBaseRate(baseRate);
                rate.setDistanceRate(distanceRate);

                return rate;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
            }
        }
        return null;
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}
