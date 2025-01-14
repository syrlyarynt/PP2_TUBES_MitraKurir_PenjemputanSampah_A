package kelompok4.praktikumpemrograman2.view;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
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
        panel = new JPanel(new MigLayout("fill, insets 10", "[grow]", "[][][grow][]"));
        initComponents();
    }

    private void initComponents() {
        // Title Panel
        JLabel titleLabel = new JLabel("Lokasi Dropbox", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(139, 0, 0));
        panel.add(titleLabel, "cell 0 0, growx, gapbottom 15");

        // Table Panel
        String[] columnNames = {"Nama Dropbox", "Alamat", "Jarak", "Kapasitas Max", "Kapasitas Terisi"};
        tableDropbox = new JTable();
        tableDropbox.setModel(new DefaultTableModel(new Object[0][0], columnNames));

        // Style table
        tableDropbox.getTableHeader().setBackground(new Color(255, 160, 122));
        tableDropbox.setBackground(new Color(255, 250, 240));
        tableDropbox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTableHeader header = tableDropbox.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setForeground(Color.WHITE);

        // Set the alternating row color renderer
        AlternatingRowColorRenderer alternatingRenderer = new AlternatingRowColorRenderer();
        for (int i = 0; i < tableDropbox.getColumnCount(); i++) {
            tableDropbox.getColumnModel().getColumn(i).setCellRenderer(alternatingRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableDropbox);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        panel.add(scrollPane, "cell 0 2, grow, push");

        // Button Panel
        JPanel buttonPanel = new JPanel(new MigLayout("", "[150][150][150]", "[]"));
        buttonPanel.setBackground(new Color(255, 250, 240));

        JButton btnAdd = createStyledButton("Add Dropbox");
        JButton btnEdit = createStyledButton("Edit Dropbox");
        JButton btnDelete = createStyledButton("Delete Dropbox");

        buttonPanel.add(btnAdd, "cell 0 0");
        buttonPanel.add(btnEdit, "cell 1 0");
        buttonPanel.add(btnDelete, "cell 2 0");

        panel.add(buttonPanel, "cell 0 3, center");

        // Add action listeners
        btnAdd.addActionListener(e -> showAddDropboxDialog());
        btnEdit.addActionListener(e -> showEditDropboxDialog());
        btnDelete.addActionListener(e -> deleteDropbox());

        loadDropboxData();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 160, 122));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        return button;
    }

    private void loadDropboxData() {
        List<LokasiDropbox> dropboxes = dropboxController.getAllDropbox();
        DefaultTableModel model = (DefaultTableModel) tableDropbox.getModel();
        model.setRowCount(0);

        for (LokasiDropbox dropbox : dropboxes) {
            model.addRow(new Object[]{
                    dropbox.getNamaDropbox(),
                    dropbox.getAlamat(),
                    dropbox.getJarak(),
                    dropbox.getKapasitasMax(),
                    dropbox.getKapasitasTerisi()
            });
        }
    }

    private void showAddDropboxDialog() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.setBackground(new Color(255, 250, 240));

        JTextField txtNamaDropbox = new JTextField();
        JTextField txtAlamat = new JTextField();
        JTextField txtJarak = new JTextField();
        JTextField txtKapasitasMax = new JTextField();
        JTextField txtKapasitasTerisi = new JTextField();

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

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Dropbox", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                LokasiDropbox dropbox = new LokasiDropbox();
                dropbox.setNamaDropbox(txtNamaDropbox.getText());
                dropbox.setAlamat(txtAlamat.getText());
                dropbox.setJarak(new BigDecimal(txtJarak.getText()));
                dropbox.setKapasitasMax(new BigDecimal(txtKapasitasMax.getText()));
                dropbox.setKapasitasTerisi(new BigDecimal(txtKapasitasTerisi.getText()));

                dropboxController.createDropbox(dropbox);
                loadDropboxData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
            }
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
        LokasiDropbox existingDropbox = dropboxes.stream()
                .filter(dropbox -> dropbox.getNamaDropbox().equals(namaDropbox))
                .findFirst()
                .orElse(null);

        if (existingDropbox != null) {
            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.setBackground(new Color(255, 250, 240));

            JTextField txtNamaDropbox = new JTextField(existingDropbox.getNamaDropbox());
            JTextField txtAlamat = new JTextField(existingDropbox.getAlamat());
            JTextField txtJarak = new JTextField(existingDropbox.getJarak().toString());
            JTextField txtKapasitasMax = new JTextField(existingDropbox.getKapasitasMax().toString());
            JTextField txtKapasitasTerisi = new JTextField(existingDropbox.getKapasitasTerisi().toString());

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

            int result = JOptionPane.showConfirmDialog(null, panel, "Edit Dropbox", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    existingDropbox.setNamaDropbox(txtNamaDropbox.getText());
                    existingDropbox.setAlamat(txtAlamat.getText());
                    existingDropbox.setJarak(new BigDecimal(txtJarak.getText()));
                    existingDropbox.setKapasitasMax(new BigDecimal(txtKapasitasMax.getText()));
                    existingDropbox.setKapasitasTerisi(new BigDecimal(txtKapasitasTerisi.getText()));

                    dropboxController.updateDropbox(existingDropbox);
                    loadDropboxData();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
                }
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
        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete '" + namaDropbox + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            List<LokasiDropbox> dropboxes = dropboxController.getAllDropbox();
            LokasiDropbox dropboxToDelete = dropboxes.stream()
                    .filter(dropbox -> dropbox.getNamaDropbox().equals(namaDropbox))
                    .findFirst()
                    .orElse(null);

            if (dropboxToDelete != null) {
                dropboxController.deleteDropbox(dropboxToDelete.getId());
                loadDropboxData();
            }
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}