package kelompok4.praktikumpemrograman2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import kelompok4.praktikumpemrograman2.controller.DropboxRateController;
import kelompok4.praktikumpemrograman2.controller.LokasiDropboxController;
import kelompok4.praktikumpemrograman2.model.DropboxRate;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;

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
        mainPanel.setBackground(new Color(255, 250, 240)); // Cream background
        initComponents();
        loadData();
    }

    private void initComponents() {
          // Panel untuk judul
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("Dropbox Rate", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 0, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel for Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Dropbox ID", "Min Distance", "Max Distance", "Base Rate", "Distance Rate"}, 0);
        table = new JTable(tableModel);
        setupTableProperties();
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.setBackground(new Color(255, 250, 240)); // Cream background

        // Panel for Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 250, 240)); // Cream background

        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");

        //style btns
        btnAdd.setBackground(new Color(255, 160, 122));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnEdit.setBackground(new Color(255, 160, 122));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnDelete.setBackground(new Color(255, 160, 122));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("SansSerif", Font.BOLD, 14));

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

    private void setupTableProperties() {
        table.setBackground(new Color(255, 239, 213)); // Light orange
        table.setForeground(Color.BLACK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Header styling
        table.getTableHeader().setBackground(new Color(255, 160, 122)); // Light coral
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Alternating row colors
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(255, 250, 240) : new Color(255, 239, 213));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
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