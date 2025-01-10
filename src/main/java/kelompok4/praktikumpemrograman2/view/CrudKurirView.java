package kelompok4.praktikumpemrograman2.view;

import kelompok4.praktikumpemrograman2.controller.KurirController;
import kelompok4.praktikumpemrograman2.model.Kurir;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CrudKurirView extends JPanel {
    private final KurirController controller;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public CrudKurirView() {
        controller = new KurirController();
        setLayout(new MigLayout("fill", "[grow]", "[][grow][]"));

        // Title Label
        JLabel titleLabel = new JLabel("Manajemen Kurir");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, "wrap, align center");

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Nomor Telepon", "Alamat", "Status"}, 0);
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, "grow, wrap");

        // Buttons
        JPanel buttonPanel = new JPanel(new MigLayout("fill", "[grow, fill][grow, fill][grow, fill][grow, fill]"));
        JButton addButton = new JButton("Tambah");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Hapus");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, "dock south");

        // Load Data
        loadData();

        // Button Actions
        addButton.addActionListener(e -> openAddKurirForm());
        editButton.addActionListener(e -> openEditKurirForm());
        deleteButton.addActionListener(e -> deleteKurir());
        refreshButton.addActionListener(e -> handleRefresh());
    }

    private void loadData() {
        tableModel.setRowCount(0); // Hapus data lama
        List<Kurir> kurirList = controller.getAllKurir();
        for (Kurir kurir : kurirList) {
            tableModel.addRow(new Object[]{kurir.getId(), kurir.getNama(), kurir.getNomorTelepon(), kurir.getAlamatKurir(), kurir.getStatus()});
        }
        table.revalidate();
        table.repaint();
    }

    private void openAddKurirForm() {
        openKurirForm(null, false);
    }

    private void openEditKurirForm() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Kurir kurir = controller.getKurirById(id);
            if (kurir != null) {
                openKurirForm(kurir, true);
            } else {
                JOptionPane.showMessageDialog(this, "Data kurir tidak ditemukan.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih kurir yang ingin diedit.");
        }
    }

    private void openKurirForm(Kurir kurir, boolean isEdit) {
        JDialog dialog = new JDialog((Frame) null, isEdit ? "Edit Kurir" : "Tambah Kurir", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new MigLayout("fillx", "[right][grow]", "[][][][][]"));
        dialog.setLocationRelativeTo(this);

        JLabel nameLabel = new JLabel("Nama:");
        JTextField nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Nomor Telepon:");
        JTextField phoneField = new JTextField();
        JLabel addressLabel = new JLabel("Alamat:");
        JTextField addressField = new JTextField();
        JLabel statusLabel = new JLabel("Status:");
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Available", "Busy", "Inactive"});

        dialog.add(nameLabel);
        dialog.add(nameField, "grow, wrap");
        dialog.add(phoneLabel);
        dialog.add(phoneField, "grow, wrap");
        dialog.add(addressLabel);
        dialog.add(addressField, "grow, wrap");
        dialog.add(statusLabel);
        dialog.add(statusComboBox, "grow, wrap");

        JButton saveButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");

        dialog.add(saveButton, "span, split 2, center");
        dialog.add(cancelButton);

        // Isi form dengan data kurir jika sedang dalam mode edit
        if (isEdit && kurir != null) {
            nameField.setText(kurir.getNama());
            phoneField.setText(kurir.getNomorTelepon());
            addressField.setText(kurir.getAlamatKurir());
            statusComboBox.setSelectedItem(kurir.getStatus());
        }

        saveButton.addActionListener(e -> {
            String nama = nameField.getText();
            String nomorTelepon = phoneField.getText();
            String alamatKurir = addressField.getText();
            String status = (String) statusComboBox.getSelectedItem();

            // Validasi input
            if (nama.isEmpty() || alamatKurir.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Semua kolom harus diisi.");
                return;
            }

            // Validasi nomor telepon hanya angka
            if (!nomorTelepon.matches("\\d+")) {
                JOptionPane.showMessageDialog(dialog, "Nomor telepon harus berupa angka.");
                return;
            }

            if (isEdit) {
                controller.updateKurir(kurir.getId(), nama, nomorTelepon, alamatKurir, status);
            } else {
                controller.insertKurir(nama, nomorTelepon, alamatKurir, status);
            }

            loadData(); // Refresh data
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void deleteKurir() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirmation = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus kurir ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                controller.deleteKurir(id);
                loadData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih kurir yang ingin dihapus.");
        }
    }

    private void handleRefresh() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    loadData(); // Muat ulang data
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(CrudKurirView.this, "Gagal memperbarui data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                    );
                }
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(CrudKurirView.this, "Data berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE)
                );
            }
        };

        worker.execute();
    }
}
