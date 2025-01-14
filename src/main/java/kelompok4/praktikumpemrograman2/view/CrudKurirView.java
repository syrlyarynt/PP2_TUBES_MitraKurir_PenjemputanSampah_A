package kelompok4.praktikumpemrograman2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import kelompok4.praktikumpemrograman2.controller.KurirController;
import kelompok4.praktikumpemrograman2.model.Kurir;
import net.miginfocom.swing.MigLayout;

public class CrudKurirView extends JPanel {
    private final KurirController controller;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public CrudKurirView() {
        controller = new KurirController();
        setLayout(new BorderLayout());
        setBackground(new Color(255, 250, 240)); // Background warna cream muda

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("Manajemen Kurir", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 0, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Nomor Telepon", "Alamat", "Status"}, 0);
        table = new JTable(tableModel);
        setupTableProperties();
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.setBackground(new Color(255, 250, 240));
        add(tablePanel, BorderLayout.CENTER);

       // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Menggunakan FlowLayout
        buttonPanel.setBackground(new Color(255, 250, 240)); // Cream background

        JButton addButton = new JButton("Tambah");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Hapus");
        JButton refreshButton = new JButton("Refresh");

        //style btns
         addButton.setBackground(new Color(255, 160, 122));
         addButton.setForeground(Color.WHITE);
         addButton.setFont(new Font("SansSerif", Font.BOLD, 14));

         editButton.setBackground(new Color(255, 160, 122));
         editButton.setForeground(Color.WHITE);
         editButton.setFont(new Font("SansSerif", Font.BOLD, 14));

         deleteButton.setBackground(new Color(255, 160, 122));
         deleteButton.setForeground(Color.WHITE);
         deleteButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        
         refreshButton.setBackground(new Color(255, 160, 122));
         refreshButton.setForeground(Color.WHITE);
         refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
       add(buttonPanel, BorderLayout.SOUTH);


        // Load Data
        loadData();

        // Button Actions
        addButton.addActionListener(e -> openAddKurirForm());
        editButton.addActionListener(e -> openEditKurirForm());
        deleteButton.addActionListener(e -> deleteKurir());
        refreshButton.addActionListener(e -> handleRefresh());
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